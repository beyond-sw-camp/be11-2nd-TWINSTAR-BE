package com.TwinStar.TwinStar.post.domain;

import com.TwinStar.TwinStar.comment.domain.Comment;
import com.TwinStar.TwinStar.common.domain.BaseTimeEntity;
import com.TwinStar.TwinStar.common.domain.Visibility;
import com.TwinStar.TwinStar.common.domain.YN;
import com.TwinStar.TwinStar.post.dto.PostUpdateReqDto;
import com.TwinStar.TwinStar.user.domain.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity
public class Post extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(length = 3000)
    private String content;

    @Column(nullable = true)
    private Post sharePostId;

    @Column(nullable = false)
    Visibility postVisibility;

    @Column(nullable = false)
    private YN postDel = YN.N;

    @Column(nullable = false)
    private YN hotIssueYn = YN.Y;

    @Column(nullable = false)
    private PostStatus postStatus;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<String> postFiles = new ArrayList<>();

    @OneToMany(mappedBy = "comment", cascade = CascadeType.PERSIST)
    @Builder.Default
    private List<Comment> Comment = new ArrayList<>();

    @PrePersist
    public void prePersist() {
        if (this.hotIssueYn == null) {
            this.hotIssueYn = YN.Y; // 기본값 설정
        }
        if (this.postDel == null) {
            this.postDel = YN.N; // 기본값 설정
        }
        if (this.postStatus == null) {
            this.postStatus = PostStatus.ACTIVE; // 기본값 설정
        }
    }

    public void update(PostUpdateReqDto dto) {
        this.content = dto.getContent();
        this.postFiles = dto.getPostFileUrls();
        this.postVisibility = dto.getPostVisibility();
    }
}