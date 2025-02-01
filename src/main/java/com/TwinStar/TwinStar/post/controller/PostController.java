package com.TwinStar.TwinStar.post.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/twinstar/post")
public class PostController {

    @GetMapping("/")
    public String index() {
        return "post_index";
    }

    @GetMapping("/create")
    public String create() {
        return "post_create";
    }

    @GetMapping("/post")
    public String post() {
        return "post_list";
    }

    @GetMapping("/post/{id}")
    public String postDetail(@PathVariable Long id) {
        return "post_detail";
    }
}
