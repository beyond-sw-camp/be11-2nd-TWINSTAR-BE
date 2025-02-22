package com.TwinStar.TwinStar.commentLike;

import com.TwinStar.TwinStar.comment.domain.Comment;
import com.TwinStar.TwinStar.user.domain.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id", nullable = false)
    private Comment comment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public static CommentLike create(Comment comment, User user) {
        return CommentLike.builder().comment(comment).user(user).build();
    }
}