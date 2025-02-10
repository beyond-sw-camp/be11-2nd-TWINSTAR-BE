package com.TwinStar.TwinStar.comment.controller;

import com.TwinStar.TwinStar.comment.service.CommentReadService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("comment")
public class CommentReadController {
    private final CommentReadService commentReadService;

    public CommentReadController(CommentReadService commentReadService) {
        this.commentReadService = commentReadService;
    }


}
