package com.TwinStar.TwinStar.post.service;

import com.TwinStar.TwinStar.common.domain.Visibility;
import com.TwinStar.TwinStar.common.service.S3Service;
import com.TwinStar.TwinStar.hashTag.domain.HashTag;
import com.TwinStar.TwinStar.hashTag.domain.PostHashTag;
import com.TwinStar.TwinStar.hashTag.repository.HashTagRepository;
import com.TwinStar.TwinStar.hashTag.repository.PostHashTagRepository;
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
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
public class PostWriteService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final HashTagRepository hashTagRepository;
    private final PostHashTagRepository postHashTagRepository;
    private final PostFileRepository postFileRepository;
    private final PostFileService postFileService;
    private final S3Service s3Service;

    public PostWriteService(PostRepository postRepository, UserRepository userRepository, PostFileService postFileService, HashTagRepository hashTagRepository, PostHashTagRepository postHashTagRepository, PostFileRepository postFileRepository, S3Service s3Service) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.hashTagRepository = hashTagRepository;
        this.postHashTagRepository = postHashTagRepository;
        this.postFileRepository = postFileRepository;
        this.postFileService = postFileService;
        this.s3Service = s3Service;
    }

    public String postCreate(PostCreateReqDto dto, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found."));

        // 1️⃣ 해시태그 리스트 생성 (PostHashTag는 아직 Post를 참조하지 않음)
        Set<PostHashTag> postHashTags = dto.getHashTags().stream()
                .map(h -> {
                    HashTag hashTag = hashTagRepository.findByHashTagName(h)
                            .orElseGet(() -> hashTagRepository.save(new HashTag(h))); // 해시태그 저장
                    return new PostHashTag(hashTag); // Post는 아직 설정하지 않음
                })
                .collect(Collectors.toSet());

        // 2️⃣ Post 엔티티 생성 (PostHashTag 리스트 전달)
        Post post = dto.toEntity(user, postHashTags);
        postRepository.save(post); // Post 먼저 저장

        // 3️⃣ PostHashTag에 Post 연결 및 저장
        postHashTags.forEach(pht -> pht.associatePost(post));
        postHashTagRepository.saveAll(postHashTags); // PostHashTag 저장

        // 4️⃣ S3에 파일 업로드 및 PostFile 저장
        if (dto.getFiles() != null && !dto.getFiles().isEmpty()) {
            List<String> fileUrls = dto.getFiles().stream()
                    .map(file -> s3Service.uploadFile(file, file.getOriginalFilename())) // S3 업로드
                    .collect(Collectors.toList());

            List<PostFile> postFiles = fileUrls.stream()
                    .map(url -> new PostFile(post, url, "image", "N")) // PostFile 엔티티 생성
                    .collect(Collectors.toList());

            postFileRepository.saveAll(postFiles); // PostFile 저장
        }

        return post.getId().toString(); // 게시글 ID 반환
    }


    @Transactional
    public void postUpdate(PostUpdateReqDto dto, Long userId) {
        Post post = postRepository.findById(dto.getPostId())
                .orElseThrow(() -> new EntityNotFoundException("Post not found."));
        if (!post.getUser().getId().equals(userId)) {
            throw new AccessDeniedException("You do not have permission to update this post.");
        }

        // 게시물 내용 및 공개 범위 수정
        post.updatePost(dto.getContent(), dto.getPostVisibility());

        // 기존 파일 숨김 처리
        postFileService.updateFileVisibility(post.getId(), dto.getFilesToHide(), "Y");

        // 새 파일 추가
        if (dto.getNewFiles() != null && !dto.getNewFiles().isEmpty()) {
            List<String> newFileUrls = dto.getNewFiles().stream()
                    .map(file -> s3Service.uploadFile(file, file.getOriginalFilename()))
                    .collect(Collectors.toList());

            List<PostFile> newPostFiles = newFileUrls.stream()
                    .map(url -> new PostFile(post, url, "image", "N"))
                    .collect(Collectors.toList());

            postFileRepository.saveAll(newPostFiles);
        }

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

    public List<String> getExistingFileUrls(Long postId, Long userId) {
        // DB에서 해당 postId의 기존 파일 URL 가져오기
        Post post = postRepository.findByIdAndUserId(postId, userId)
                .orElseThrow(() -> new RuntimeException("해당 게시글을 찾을 수 없습니다."));

        // 해당 postId의 모든 파일 URL 조회
        List<PostFile> postFiles = postFileRepository.findByPostId(postId);

        // URL 리스트로 변환
        return postFiles.stream()
                .map(PostFile::getFileUrl)
                .collect(Collectors.toList());
    }


}
