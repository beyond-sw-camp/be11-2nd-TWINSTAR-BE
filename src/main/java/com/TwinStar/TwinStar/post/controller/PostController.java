package com.TwinStar.TwinStar.post.controller;

import com.TwinStar.TwinStar.common.auth.JwtUtil;
import com.TwinStar.TwinStar.post.dto.PostCreateReqDto;
import com.TwinStar.TwinStar.post.service.PostService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("post")
public class PostController {
    private final PostService postService;
    private final JwtUtil jwtUtil;

    public PostController(PostService postService, JwtUtil jwtUtil) {
        this.postService = postService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("create")
    public ResponseEntity<?> postCreate(@RequestBody PostCreateReqDto dto, @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader){
        // Bearer 토큰에서 실제 토큰 값만 추출
        String token = authorizationHeader.replace("Bearer ", "");
        // 토큰에서 userId 추출
        Long userId = jwtUtil.getUserId(token);
        postService.postCreate(dto, userId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
