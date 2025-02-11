package com.TwinStar.TwinStar.post.controller;

import com.TwinStar.TwinStar.common.auth.JwtUtil;
import com.TwinStar.TwinStar.post.dto.PostCreateReqDto;
import com.TwinStar.TwinStar.post.dto.PostUpdateReqDto;
import com.TwinStar.TwinStar.post.service.PostWriteService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("post")
public class PostWriteController {
    private final PostWriteService postWriteService;
    private final JwtUtil jwtUtil;

    public PostWriteController(PostWriteService postWriteService, JwtUtil jwtUtil) {
        this.postWriteService = postWriteService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("create")
    public ResponseEntity<?> postCreate(@RequestBody PostCreateReqDto dto,
                                        @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader) {
        try {
            String token = authorizationHeader.replace("Bearer ", "");
            Long userId = jwtUtil.getUserId(token);

            postWriteService.postCreate(dto, userId);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("update")
    public ResponseEntity<?> postUpdate(@RequestBody PostUpdateReqDto dto,
                                        @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader) {
        String token = authorizationHeader.replace("Bearer ", "");
        Long userId = jwtUtil.getUserId(token);

        postWriteService.postUpdate(dto, userId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("delete/{postId}")
    public ResponseEntity<?> postDelete(@PathVariable Long postId,
                                        @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader) {
        String token = authorizationHeader.replace("Bearer ", "");
        Long userId = jwtUtil.getUserId(token);

        postWriteService.postDelete(postId, userId);
        return ResponseEntity.ok().build();
    }
}