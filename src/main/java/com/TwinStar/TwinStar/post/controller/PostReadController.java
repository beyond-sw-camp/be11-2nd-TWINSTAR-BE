package com.TwinStar.TwinStar.post.controller;

import com.TwinStar.TwinStar.common.auth.JwtUtil;
import com.TwinStar.TwinStar.post.dto.HomePostResDto;
import com.TwinStar.TwinStar.post.service.PostReadService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("post")
public class PostReadController {

    private final PostReadService postReadService;
    private final JwtUtil jwtUtil;

    public PostReadController(PostReadService postReadService, JwtUtil jwtUtil) {
        this.postReadService = postReadService;
        this.jwtUtil = jwtUtil;
    }

    @GetMapping("/home")
    public ResponseEntity<?> getHomePost(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader) {
        // Bearer 토큰에서 실제 토큰 값만 추출
        String token = authorizationHeader.replace("Bearer ", "");
        // 토큰에서 userId 추출
        Long userId = jwtUtil.getUserId(token);
        return ResponseEntity.ok(postReadService.getHomePost(userId));
    }
}