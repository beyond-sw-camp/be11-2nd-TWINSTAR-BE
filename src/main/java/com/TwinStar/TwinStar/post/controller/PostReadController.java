package com.TwinStar.TwinStar.post.controller;

import com.TwinStar.TwinStar.common.auth.JwtUtil;
import com.TwinStar.TwinStar.common.dto.CommonDto;
import com.TwinStar.TwinStar.post.dto.HomePostResDto;
import com.TwinStar.TwinStar.post.service.PostReadService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
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

//    home화면에서 post 리스트 보여주는 메서드
    @GetMapping("/home")
    public ResponseEntity<?> getHomePost(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader) {
        // Bearer 토큰에서 실제 토큰 값만 추출
        String token = authorizationHeader.replace("Bearer ", "");
        // 토큰에서 userId 추출
        Long userId = jwtUtil.getUserId(token);
        Page<HomePostResDto> homePostResDtoPage = postReadService.getHomePost(userId);
        return new ResponseEntity<>(new CommonDto(HttpStatus.OK.value(), "post list is found.",homePostResDtoPage),HttpStatus.OK);
    }
}