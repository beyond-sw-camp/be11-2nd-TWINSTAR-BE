package com.TwinStar.TwinStar.comment.repository;

import com.TwinStar.TwinStar.comment.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByPostIdOrderByCreatedTimeDesc(Long postId);

    @Query("SELECT c FROM Comment c WHERE c.post.id = :postId ORDER BY c.createdTime DESC")
    List<Comment> findAllCommentsByPostId(Long postId);  // 🔹 삭제된 댓글 포함해서 정렬하여 가져오기


}