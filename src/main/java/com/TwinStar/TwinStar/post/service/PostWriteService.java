package com.TwinStar.TwinStar.post.service;

import com.TwinStar.TwinStar.common.domain.Visibility;
import com.TwinStar.TwinStar.common.domain.YN;
import com.TwinStar.TwinStar.post.domain.Post;
import com.TwinStar.TwinStar.post.dto.PostCreateReqDto;
import com.TwinStar.TwinStar.post.dto.PostUpdateReqDto;
import com.TwinStar.TwinStar.post.repository.PostRepository;
import com.TwinStar.TwinStar.post_file.PostFile;
import com.TwinStar.TwinStar.post_file.PostFileRepository;
import com.TwinStar.TwinStar.post_file.PostFileService;
import com.TwinStar.TwinStar.user.domain.User;
import com.TwinStar.TwinStar.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class PostWriteService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final PostFileService postFileService;
    private final PostFileRepository postFileRepository;

    public PostWriteService(PostRepository postRepository, UserRepository userRepository, PostFileService postFileService, PostFileRepository postFileRepository1) {
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

        Post post = postRepository.findById(dto.getPostId())
                .orElseThrow(() -> new EntityNotFoundException("Post not found."));

        if (!post.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException("You do not have permission to update this post.");
        }

        // 게시글 내용 수정
        post.setContent(dto.getContent());
        post.setPostVisibility(dto.getPostVisibility());

        // 기존 파일 목록 가져오기
        List<String> existingFiles = postFileRepository.findActiveUrlsByPostId(dto.getPostId());
        List<String> updatedFileUrls = dto.getPostFileUrls();

        // 기존 파일 중 업데이트되지 않은 파일 → 숨김 처리
        List<String> filesToHide = existingFiles.stream()
                .filter(file -> !updatedFileUrls.contains(file))
                .toList();

        // ✅ 기존 파일을 숨김 처리
        if (!filesToHide.isEmpty()) {
            postFileService.hidePostFiles(dto.getPostId(), filesToHide);
        }

        // ✅ 숨겨진 파일 복원 처리
        postFileService.restorePostFiles(dto.getPostId(), updatedFileUrls);

        // ✅ 새로운 파일 저장
        postFileService.savePostFiles(post, updatedFileUrls);

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

        // 게시글 논리적 삭제
        post.setPostDel(YN.Y);

        // 게시글 공개 범위 수정
        post.setPostVisibility(Visibility.LOCK);

        // 저장
        postRepository.save(post);
    }

}