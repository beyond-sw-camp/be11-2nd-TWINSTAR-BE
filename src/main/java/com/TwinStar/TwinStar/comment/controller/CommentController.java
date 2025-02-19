package com.TwinStar.TwinStar.comment.controller;

import com.TwinStar.TwinStar.comment.dto.CommentRequestDto;
import com.TwinStar.TwinStar.comment.dto.CommentResponseDto;
import com.TwinStar.TwinStar.comment.service.CommentService;
import com.TwinStar.TwinStar.common.auth.JwtAuthFilter;
import com.TwinStar.TwinStar.user.domain.User;
import com.TwinStar.TwinStar.user.domain.UserStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comment")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;
    private final JwtAuthFilter jwtAuthFilter;


    // 댓글 작성
    @PostMapping("/{postId}")
    public ResponseEntity<CommentResponseDto> createComment(@PathVariable Long postId, @RequestBody CommentRequestDto requestDto) {
        return ResponseEntity.ok(commentService.createComment(postId, requestDto));
    }

    // 특정 게시물의 댓글 조회
    @GetMapping("/{postId}")
    public ResponseEntity<List<CommentResponseDto>> getComments(@PathVariable Long postId) {
        return ResponseEntity.ok(commentService.getComments(postId));
    }

    // 댓글 수정
    @PutMapping("/{commentId}")
    public ResponseEntity<CommentResponseDto> updateComment(
            @PathVariable Long commentId,
            @RequestBody CommentRequestDto requestDto) {

        // 현재 인증된 사용자의 ID 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long currentUserId;

        try {
            currentUserId = Long.valueOf(authentication.getName()); // 토큰에서 userId 추출
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("잘못된 사용자 인증 정보입니다.");
        }

        // Service에서 댓글 수정 처리 (권한 체크 포함)
        CommentResponseDto updatedComment = commentService.updateComment(commentId, requestDto, currentUserId);
        return ResponseEntity.ok(updatedComment);
    }


    // 댓글 삭제
    @DeleteMapping("/{commentId}")
    public ResponseEntity<?> deleteComment(@PathVariable Long commentId) {
        commentService.deleteComment(commentId);
        return ResponseEntity.ok().build();
    }
}
