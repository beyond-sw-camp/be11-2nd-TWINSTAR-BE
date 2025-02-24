package com.TwinStar.TwinStar.post.controller;

import com.TwinStar.TwinStar.post.service.PostService;
import jakarta.persistence.Column;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/post")
@RestController
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

}
