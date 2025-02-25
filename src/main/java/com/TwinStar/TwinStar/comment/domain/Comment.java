package com.TwinStar.TwinStar.comment.domain;

import com.TwinStar.TwinStar.common.domain.BaseTimeEntity;
import com.TwinStar.TwinStar.post.domain.Post;
import com.TwinStar.TwinStar.user.domain.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
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

    @Column(nullable = false, length = 500)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Comment parent;

    @OneToMany(mappedBy = "parent" , cascade = CascadeType.ALL)
    @Builder.Default
    private List<Comment> child = new ArrayList<>();

    @OneToMany(mappedBy = "comment" , cascade = CascadeType.ALL)
    private List<CommentLike> commentLike = new ArrayList<>();

    @Builder.Default
    private String pinnedComment = "N";;

    @Builder.Default
    private String commentDel = "N";

    public void updateContent(String content) {
        this.content = content;
    }

    public void delete() {
        this.content = "삭제된 댓글입니다.";
        this.commentDel="Y";
    }

    public void addChild(Comment comment) {
        this.child.add(comment);
    }

    public void pinned(){
        this.pinnedComment = "Y";
    }
}
