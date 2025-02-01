package com.TwinStar.TwinStar.post.service;

import com.TwinStar.TwinStar.post.dtos.PostCreateReq;
import com.TwinStar.TwinStar.post.repository.PostRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Transactional
public class PostService {
    private final PostRepository postRepository;
    private final userRepository userRepository;

    public PostService(PostRepository postRepository, userRepository userRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    public void save(PostCreateReq postCreateReq, UserRepository userRepository){
        String Nickname = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByNickname(nickname).orElseThrow(()->new EntityNotFoundException("존재하지 않는 유저 입니다."));
        LocalDateTime appointmentTime = null;
    }
}
