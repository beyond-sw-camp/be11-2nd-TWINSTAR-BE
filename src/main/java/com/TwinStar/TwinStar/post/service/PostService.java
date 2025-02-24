package com.TwinStar.TwinStar.post.service;

import com.TwinStar.TwinStar.hashTag.domain.HashTag;
import com.TwinStar.TwinStar.hashTag.domain.PostHashTag;
import com.TwinStar.TwinStar.hashTag.repository.PostHashTagRepository;
import com.TwinStar.TwinStar.hashTag.service.HashTagService;
import com.TwinStar.TwinStar.post.domain.Post;
import com.TwinStar.TwinStar.post.domain.PostFile;
import com.TwinStar.TwinStar.post.dto.PostCreateReqDto;
import com.TwinStar.TwinStar.post.dto.PostUpdateReqDto;
import com.TwinStar.TwinStar.post.dto.PostUpdateResDto;
import com.TwinStar.TwinStar.post.repository.PostFileRepository;
import com.TwinStar.TwinStar.post.repository.PostRepository;
import com.TwinStar.TwinStar.user.domain.User;
import com.TwinStar.TwinStar.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final PostFileRepository postFileRepository;
    private final HashTagService hashTagService;
    private final PostHashTagRepository postHashTagRepository;
    private final S3Client s3Client;
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;
    @Value("${cloud.aws.region.static}")
    private String region;

    public PostService(PostRepository postRepository, UserRepository userRepository, PostFileRepository postFileRepository, HashTagService hashTagService, PostHashTagRepository postHashTagRepository, S3Client s3Client) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.postFileRepository = postFileRepository;
        this.hashTagService = hashTagService;
        this.postHashTagRepository = postHashTagRepository;
        this.s3Client = s3Client;
    }

    public Long save(PostCreateReqDto dto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findById(Long.valueOf(authentication.getName())).orElseThrow(()-> new EntityNotFoundException("user is not found."));
        Post post = postRepository.save(dto.toEntity(user));
        for (MultipartFile file : dto.getImageFile()){
            String fileUrl = uploadImage(file);
            postFileRepository.save(new PostFile(post,fileUrl));
        }
        for (String tag: dto.getHashTag()){
            HashTag hashTag = hashTagService.findOrCreateHashTag(tag);
            PostHashTag postHashTag = PostHashTag.builder()
                    .post(post)
                    .hashTag(hashTag)
                    .build();
            postHashTagRepository.save(postHashTag);
        }
        return post.getId();
    }

    public String uploadImage(MultipartFile file) {
        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();

        try {
            s3Client.putObject(
                    PutObjectRequest.builder()
                            .bucket(bucket)
                            .key(fileName)
                            .contentType(file.getContentType())
                            .build(),
                    RequestBody.fromInputStream(file.getInputStream(), file.getSize())
            );

            return "https://" + bucket + ".s3." + region + ".amazonaws.com/" + fileName;
        } catch (IOException e) {
            throw new RuntimeException("파일 업로드 실패", e);
        }
    }

    public void delete(Long postId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User loginUser = userRepository.findById(Long.valueOf(authentication.getName())).orElseThrow(()->new EntityNotFoundException("user not found"));
        Post post = postRepository.findById(postId).orElseThrow(()-> new EntityNotFoundException("post is not found."));
        User postWriteUser = post.getUser();

        if (!loginUser.equals(postWriteUser)){ return ; }
        postRepository.delete(post);
    }

    public void Update(Long postId, PostUpdateReqDto dto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User loginUser = userRepository.findById(Long.valueOf(authentication.getName())).orElseThrow(()->new EntityNotFoundException("user not found"));
        Post post = postRepository.findById(postId).orElseThrow(()-> new EntityNotFoundException("post is not found."));
        User postWriteUser = post.getUser();

        if (!loginUser.equals(postWriteUser)){ return ; }
        post.updateContent(dto.getContent());

        for (String tag: dto.getHashTag()){
            HashTag hashTag = hashTagService.findOrCreateHashTag(tag);
            PostHashTag postHashTag = PostHashTag.builder()
                    .post(post)
                    .hashTag(hashTag)
                    .build();
            postHashTagRepository.save(postHashTag);
        }

    }

    public PostUpdateResDto getUpdateDataRes(Long postId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User loginUser = userRepository.findById(Long.valueOf(authentication.getName())).orElseThrow(()->new EntityNotFoundException("user not found"));
        Post post = postRepository.findById(postId).orElseThrow(()-> new EntityNotFoundException("post is not found."));
        User postWriteUser = post.getUser();
        if (!loginUser.equals(postWriteUser)){ return new PostUpdateResDto(); }
        List<String> postUrlList = post.getFileUrls();
        List<String> postHashTagList = hashTagService.getHashTagsByPost(post);
        return post.formEntity(postHashTagList, postUrlList);

    }
}
