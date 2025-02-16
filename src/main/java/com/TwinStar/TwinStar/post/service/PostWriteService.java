package com.TwinStar.TwinStar.post.service;

import com.TwinStar.TwinStar.common.domain.Visibility;
import com.TwinStar.TwinStar.hashTag.domain.HashTag;
import com.TwinStar.TwinStar.hashTag.domain.PostHashTag;
import com.TwinStar.TwinStar.hashTag.repository.HashTagRepository;
import com.TwinStar.TwinStar.hashTag.repository.PostHashTagRepository;
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

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class PostWriteService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final PostFileService postFileService;
    private final HashTagRepository hashTagRepository;
    private final PostHashTagRepository postHashTagRepository;

    public PostWriteService(PostRepository postRepository, UserRepository userRepository, PostFileService postFileService, HashTagRepository hashTagRepository, PostHashTagRepository postHashTagRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.postFileService = postFileService;
        this.hashTagRepository = hashTagRepository;
        this.postHashTagRepository = postHashTagRepository;
    }

    public void postCreate(PostCreateReqDto dto, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found."));

//      게시물에 있는 해시태그를 추출해서 해시태그와 해시태그와 연관된 게시물을 저장하고 조회함
        List<PostHashTag> postHashTags = dto.getHashTags().stream().map(h -> {
            HashTag hashTag = hashTagRepository.findByHashTagName(h)
                    .orElseGet(() -> hashTagRepository.save(new HashTag(h)));

//          해시태그만 저장하고 post는 따로 저장함
            return PostHashTag.builder()
                    .hashTag(hashTag)
                    .post(null)
                    .build();
        }).collect(Collectors.toList());

        Post post = dto.toEntity(user,postHashTags);
//      post객체에 해시태그 리스트 추가
        post.addPostHashTags(postHashTags);
        //      postHashTags에 post를 설정하고 추가
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
