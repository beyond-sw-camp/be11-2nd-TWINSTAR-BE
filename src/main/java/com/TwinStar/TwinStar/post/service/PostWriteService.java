package com.TwinStar.TwinStar.post.service;

import com.TwinStar.TwinStar.common.domain.Visibility;
import com.TwinStar.TwinStar.post.domain.Post;
import com.TwinStar.TwinStar.post.dto.PostCreateReqDto;
import com.TwinStar.TwinStar.post.dto.PostUpdateReqDto;
import com.TwinStar.TwinStar.post.repository.PostRepository;
import com.TwinStar.TwinStar.post_file.PostFileService;
import com.TwinStar.TwinStar.user.domain.User;
import com.TwinStar.TwinStar.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class PostWriteService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final PostFileService postFileService;

    public PostWriteService(PostRepository postRepository, UserRepository userRepository, PostFileService postFileService) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.postFileService = postFileService;
    }

    public void postCreate(PostCreateReqDto dto, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found."));

        Post post = dto.toEntity(user);
        postRepository.save(post);
        postFileService.savePostFiles(post, dto.getFiles());
    }

    public void postUpdate(PostUpdateReqDto dto, Long userId) {
        Post post = postRepository.findById(dto.getPostId())
                .orElseThrow(() -> new EntityNotFoundException("Post not found."));

        post.update(dto, postFileService);
        postRepository.save(post);
    }

    public void postDelete(Long postId, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found."));

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("Post not found."));

        if (!post.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException("You do not have permission to delete this post.");
        }

        // update 메서드 사용
        post.updatePostDel("Y"); // 메서드를 통한 값 변경
        post.updatePostVisibility(Visibility.LOCK); // 공개 범위 변경

        postRepository.save(post);
    }

}
