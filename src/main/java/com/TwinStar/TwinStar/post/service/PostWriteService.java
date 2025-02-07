package com.TwinStar.TwinStar.post.service;

import com.TwinStar.TwinStar.post.domain.Post;
import com.TwinStar.TwinStar.post.dto.PostCreateReqDto;
import com.TwinStar.TwinStar.post.dto.PostUpdateReqDto;
import com.TwinStar.TwinStar.post.repository.PostRepository;
import com.TwinStar.TwinStar.user.domain.User;
import com.TwinStar.TwinStar.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class PostWriteService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public PostWriteService(PostRepository postRepository, UserRepository userRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    public void postCreate(PostCreateReqDto dto, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(()-> new EntityNotFoundException("user is not found."));
        Post post = dto.toEntity(user);
        postRepository.save(post);
    }

    public void postUpdate(PostUpdateReqDto dto, Long userId){
        User user = userRepository.findById(userId).orElseThrow(()-> new EntityNotFoundException("user is not found."));
        Post post = dto.toEntity(user);
        postRepository.save(post);
    }
}
