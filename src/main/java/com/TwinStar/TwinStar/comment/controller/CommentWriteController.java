package com.TwinStar.TwinStar.comment.controller;

import com.TwinStar.TwinStar.comment.service.CommentWriteService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("comment")
public class CommentWriteController {
    private final CommentWriteService commentWriteService;

    public CommentWriteController(CommentWriteService commentWriteService) {
        this.commentWriteService = commentWriteService;
    }


}
