package com.TwinStar.TwinStar.post.service;

import com.TwinStar.TwinStar.common.domain.YN;
import com.TwinStar.TwinStar.post.domain.Post;
import com.TwinStar.TwinStar.post.dto.PostCreateReqDto;
import com.TwinStar.TwinStar.post.dto.PostUpdateReqDto;
import com.TwinStar.TwinStar.post.repository.PostRepository;
import com.TwinStar.TwinStar.post_file.PostFileRepository;
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
    private final PostFileRepository postFileRepository;

    public PostWriteService(PostRepository postRepository, UserRepository userRepository, PostFileRepository postFileRepository, PostFileService postFileService, PostFileRepository postFileRepository1) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.postFileService = postFileService;
        this.postFileRepository = postFileRepository1;
    }

    public void postCreate(PostCreateReqDto dto, Long userId) {
        dto.validate(); // 파일 최소 1개 이상, 최대 10개 검사

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found."));

        Post post = dto.toEntity(user);
        postRepository.save(post);

        // 파일 저장
        postFileService.savePostFiles(post, dto.getFiles());
    }

    public void postUpdate(PostUpdateReqDto dto, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found."));

        Post post = postRepository.findById(dto.getPostId())  // 기존 Post 찾기
                .orElseThrow(() -> new EntityNotFoundException("Post not found."));

        if (!post.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException("You do not have permission to update this post.");
        }

        // 🔹 게시글 내용 수정
        post.setContent(dto.getContent());
        post.setPostVisibility(dto.getPostVisibility());

        // 🔹 기존 파일 삭제 후 새로운 파일 저장
        postFileRepository.deleteByPostId(dto.getPostId()); // 기존 파일 삭제
        postFileService.savePostFiles(post, dto.getPostFileUrls()); // 새로운 파일 저장

        postRepository.save(post);  // 변경된 내용 저장
    }


    public void postDelete(Long postId, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found."));

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("Post not found."));

        if (!post.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException("You do not have permission to delete this post.");
        }

        // 1️⃣ 먼저 post_file의 파일 삭제
        postFileRepository.deleteByPostId(postId);

        // 2️⃣ 이후 게시글 논리적 삭제
        post.setPostDel(YN.Y);
        postRepository.save(post);
    }

}
