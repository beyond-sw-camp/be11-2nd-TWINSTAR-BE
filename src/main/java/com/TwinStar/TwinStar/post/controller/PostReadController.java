package com.TwinStar.TwinStar.post.controller;

import com.TwinStar.TwinStar.post.dto.*;
import com.TwinStar.TwinStar.post.service.PostReadService;
import com.TwinStar.TwinStar.commentLike.CommentLikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/post")
@RequiredArgsConstructor
public class PostReadController {

    private final PostReadService postReadService;
    private final com.TwinStar.TwinStar.postLike.PostLikeService postLikeService;
    private final CommentLikeService commentLikeService;

    // 홈 피드 조회 (userId 필요)
    @GetMapping("/home")
    public ResponseEntity<List<PostResponseDto>> getHomeFeed(@RequestParam Long userId) {
        return ResponseEntity.ok(postReadService.getHomeFeed(userId));
    }

    // 특정 유저 게시물 조회
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<PostResponseDto>> getUserPosts(@PathVariable Long userId) {
        return ResponseEntity.ok(postReadService.getUserPosts(userId));
    }

    // 내 게시물 조회 (userId 필요)
    @GetMapping("/myList")
    public ResponseEntity<List<PostResponseDto>> getMyPosts(@RequestParam Long userId) {
        return ResponseEntity.ok(postReadService.getMyPosts(userId));
    }

    // 핫 게시물 조회
    @GetMapping("/hot")
    public ResponseEntity<List<PostResponseDto>> getHotPosts() {
        return ResponseEntity.ok(postReadService.getHotPosts());
    }

    // 특정 게시물 상세 조회
    @GetMapping("/{postId}")
    public ResponseEntity<?> getPostDetail(@PathVariable Long postId) {
        return ResponseEntity.ok(postReadService.getPostDetail(postId));
    }

    // 게시물 검색
    @GetMapping("/search")
    public ResponseEntity<List<PostResponseDto>> searchPosts(@RequestParam String query) {
        return ResponseEntity.ok(postReadService.searchPosts(query));
    }

    // 게시물 좋아요
    @PostMapping("/{postId}/like")
    public ResponseEntity<Void> likePost(@PathVariable Long postId, @RequestParam Long userId) {
        postLikeService.likePost(postId, userId);
        return ResponseEntity.ok().build();
    }

    // 댓글 좋아요
    @PostMapping("/comment/{commentId}/like")
    public ResponseEntity<Void> likeComment(@PathVariable Long commentId, @RequestParam Long userId) {
        commentLikeService.toggleCommentLike(commentId, userId);
        return ResponseEntity.ok().build();
    }
}
