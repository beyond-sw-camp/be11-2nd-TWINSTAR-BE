package com.TwinStar.TwinStar.comment.repository;

import com.TwinStar.TwinStar.comment.domain.Comment;
import com.TwinStar.TwinStar.comment.domain.CommentLike;
import com.TwinStar.TwinStar.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CommentLikeRepository extends JpaRepository<CommentLike, Long> {
    @Query("SELECT COUNT(cl) FROM CommentLike cl WHERE cl.comment.post.id = :postId")
    Long countCommentLikesByPostId(@Param("postId") Long postId);

    Optional<CommentLike> findByCommentAndUser(Comment comment, User user);

    Long countByComment(Comment comment);
}
