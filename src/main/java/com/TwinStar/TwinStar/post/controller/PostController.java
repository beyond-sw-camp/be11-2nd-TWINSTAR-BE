package com.TwinStar.TwinStar.post.controller;

import com.TwinStar.TwinStar.common.dto.CommonDto;
import com.TwinStar.TwinStar.post.dto.PostCreateReqDto;
import com.TwinStar.TwinStar.post.service.PostService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/post")
@RestController
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@ModelAttribute PostCreateReqDto dto){
        Long postId = postService.save(dto);
        return new ResponseEntity<>(new CommonDto(HttpStatus.OK.value(), "게시물 작성 완료",postId),HttpStatus.OK);
    }




}
