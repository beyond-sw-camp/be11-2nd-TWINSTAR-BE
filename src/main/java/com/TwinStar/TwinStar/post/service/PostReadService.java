package com.TwinStar.TwinStar.post.service;

import com.TwinStar.TwinStar.post.repository.PostRepository;
import org.springframework.stereotype.Service;

@Service
public class PostReadService {

    private final PostRepository postRepository;

    public PostReadService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public Object getHomePost(Long userId) {
    }
}
