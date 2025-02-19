package com.TwinStar.TwinStar.commentLike;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CommentLikeRepository extends JpaRepository<CommentLike, Long> {

    // 특정 댓글의 좋아요 개수 조회
    int countByCommentId(Long commentId);

    // 사용자가 특정 댓글에 좋아요를 눌렀는지 확인
    boolean existsByCommentIdAndUserId(Long commentId, Long userId);

    // 사용자가 특정 댓글에 좋아요를 누른 기록 조회 (Optional 반환)
    Optional<CommentLike> findByCommentIdAndUserId(Long commentId, Long userId);

    // 사용자가 특정 댓글에 좋아요를 누른 기록 삭제
    void deleteByCommentIdAndUserId(Long commentId, Long userId);
}
