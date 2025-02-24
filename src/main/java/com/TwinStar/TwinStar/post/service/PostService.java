package com.TwinStar.TwinStar.post.service;

import com.TwinStar.TwinStar.post.domain.Post;
import com.TwinStar.TwinStar.post.domain.PostFile;
import com.TwinStar.TwinStar.post.dto.PostCreateReqDto;
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
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final PostFileRepository postFileRepository;
    private final S3Client s3Client;
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public PostService(PostRepository postRepository, UserRepository userRepository, PostFileRepository postFileRepository, S3Client s3Client) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.postFileRepository = postFileRepository;
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
        return post.getId();
    }

    public String uploadImage(MultipartFile file) {
        String fileName = "https://twinstar.s3.ap-northeast-2.amazonaws.com/" + UUID.randomUUID()+"_"+file.getOriginalFilename();
            try {
                s3Client.putObject(
                        PutObjectRequest.builder()
                                .bucket(bucket)
                                .key(fileName)
                                .contentType(file.getContentType())
                                .build(),
                        RequestBody.fromInputStream(file.getInputStream(), file.getSize())
                );
                return fileName;
            }catch (IOException e){
                throw new RuntimeException("파일 업로드 실패");
            }

    }
}
