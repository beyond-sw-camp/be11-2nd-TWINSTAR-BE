package com.TwinStar.TwinStar.comment.domain;

import com.TwinStar.TwinStar.common.domain.BaseTimeEntity;
import com.TwinStar.TwinStar.post.domain.Post;
import com.TwinStar.TwinStar.user.domain.User;
import jakarta.persistence.*;
import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity
public class Comment extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, length = 1000)
    private String content;

    @Column(nullable = false, length = 1)
    @Builder.Default
    private String delYn = "N";  // 🔹 @Builder.Default 사용하여 기본값 설정

    public static Comment create(Post post, User user, String content) {
        return Comment.builder()
                .post(post)
                .user(user)
                .content(content)
                .delYn("N")  // 🔹 기본값 설정
                .build();
    }

    public void updateContent(String content) {
        this.content = content;
    }

    public void deleteComment() {
        this.delYn = "Y";
    }
}
