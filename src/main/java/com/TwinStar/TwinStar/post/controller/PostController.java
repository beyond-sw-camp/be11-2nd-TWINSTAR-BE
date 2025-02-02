package com.TwinStar.TwinStar.post.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/posts")
public class PostController {

    // 게시글 목록 조회
    @GetMapping
    public ResponseEntity<List<PostListRes>> getAllPosts() {
        List<PostListRes> posts = postService.getAllPosts();
        return ResponseEntity.ok(posts);
    }

    // 게시글 상세 조회
    @GetMapping("/{id}")
    public ResponseEntity<PostDetailRes> getPost(@PathVariable Long id) {
        PostDetailRes post = postService.getPost(id);
        return ResponseEntity.ok(post);
    }

    // 게시글 작성
    @PostMapping
    public ResponseEntity<PostDetailRes> createPost(@RequestBody PostCreateReq request) {
        PostDetailRes createdPost = postService.createPost(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdPost);
    }

    // 게시글 수정
    @PutMapping("/{id}")
    public ResponseEntity<PostDetailRes> updatePost(@PathVariable Long id, @RequestBody PostUpdateReq request) {
        PostDetailRes updatedPost = postService.updatePost(id, request);
        return ResponseEntity.ok(updatedPost);
    }

    // 게시글 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Long id) {
        postService.deletePost(id);
        return ResponseEntity.noContent().build();
    }
}
