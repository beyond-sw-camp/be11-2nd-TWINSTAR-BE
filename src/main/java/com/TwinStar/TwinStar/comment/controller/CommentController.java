package com.TwinStar.TwinStar.comment.controller;

import com.TwinStar.TwinStar.comment.dto.CommentCreateReqDto;
import com.TwinStar.TwinStar.comment.service.CommentService;
import com.TwinStar.TwinStar.common.dto.CommonDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/comment")
@RestController
public class CommentController {
    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/create")
    public ResponseEntity<?> createComment(@RequestBody CommentCreateReqDto dto){
        Long postId = commentService.create(dto);
        return new ResponseEntity<>(new CommonDto(HttpStatus.OK.value(), "댓글 작성 완료",postId),HttpStatus.OK);
    }
}
