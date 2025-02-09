package com.TwinStar.TwinStar.comment.domain;

import com.TwinStar.TwinStar.common.domain.BaseTimeEntity;
import com.TwinStar.TwinStar.common.domain.YN;
import com.TwinStar.TwinStar.post.domain.Post;
import com.TwinStar.TwinStar.user.domain.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Entity
@Table(name = "comment")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Comment extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(length = 3000, nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Comment parentId;

    @Column(nullable = false)
    private YN pinnedComment;

    @Column(nullable = false)
    private YN delYn;

    @OneToMany(mappedBy = "comment", cascade = CascadeType.PERSIST)
    @Builder.Default
    private List<CommentLike> commentLike;


    @PrePersist
    public void prePersist() {
        if (this.pinnedComment == null) {
            this.pinnedComment = YN.N; // 기본값 설정
        }
        if (this.delYn == null) {
            this.delYn = YN.N; // 기본값 설정
        }
    }
}


