package com.TwinStar.TwinStar.commentLike;

import com.TwinStar.TwinStar.comment.domain.Comment;
import com.TwinStar.TwinStar.comment.repository.CommentRepository;
import com.TwinStar.TwinStar.user.domain.User;
import com.TwinStar.TwinStar.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentLikeService {

    private final CommentLikeRepository commentLikeRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;

    @Transactional
    public void toggleCommentLike(Long commentId, Long userId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("댓글을 찾을 수 없습니다."));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        if ("Y".equals(comment.getDelYn())) {
            throw new IllegalArgumentException("삭제된 댓글에는 좋아요를 누를 수 없습니다.");
        }

        commentLikeRepository.findByCommentIdAndUserId(commentId, userId)
                .ifPresentOrElse(
                        commentLikeRepository::delete, // 존재하면 삭제
                        () -> commentLikeRepository.save(CommentLike.create(comment, user)) // 없으면 추가
                );
    }
}