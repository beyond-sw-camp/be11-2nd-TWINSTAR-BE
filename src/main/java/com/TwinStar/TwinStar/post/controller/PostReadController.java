package com.TwinStar.TwinStar.post.controller;

import com.TwinStar.TwinStar.post.dto.HomePostResDto;
import com.TwinStar.TwinStar.post.service.PostReadService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("post")
public class PostReadController {

    private final PostReadService postReadService;

    public PostReadController(PostReadService postReadService) {
        this.postReadService = postReadService;
    }

    @GetMapping("/home")
    public ResponseEntity<?> getHomePost(@RequestParam Long userId) {
        return ResponseEntity.ok(postReadService.getHomePost(userId));
    }
}