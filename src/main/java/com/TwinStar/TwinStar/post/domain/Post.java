package com.TwinStar.TwinStar.post.domain;

import com.TwinStar.TwinStar.comment.domain.Comment;
import com.TwinStar.TwinStar.common.domain.BaseTimeEntity;
import com.TwinStar.TwinStar.common.domain.Visibility;
import com.TwinStar.TwinStar.common.domain.YN;
import com.TwinStar.TwinStar.post.dto.PostUpdateReqDto;
import com.TwinStar.TwinStar.post_file.PostFile;
import com.TwinStar.TwinStar.post_file.PostFileService;
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
@EqualsAndHashCode(callSuper = false)
public class Post extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(length = 3000)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "share_post_id")
    private Post sharePost;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private Visibility postVisibility = Visibility.ALL; // ✅ 기본값 설정


    @Column(nullable = false)
    @Builder.Default
    private YN postDel = YN.N;

    @Column(nullable = false)
    @Builder.Default
    private YN hotIssueYn = YN.Y;

    @Column(nullable = false)
    private PostStatus postStatus;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<PostFile> postFiles = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.PERSIST)
    @Builder.Default
    private List<Comment> comments = new ArrayList<>();

    @Column(nullable = false)
    @Builder.Default
    private int Score = 0;

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
        if (this.postVisibility == null) {
            this.postVisibility = Visibility.ALL;
        }
    }

    public void update(PostUpdateReqDto dto, PostFileService postFileService) {
        this.content = dto.getContent();

        // ✅ PostFileService를 이용해 변환 처리
        this.postFiles = postFileService.convertToPostFiles(this, dto.getPostFileUrls());

        this.postVisibility = dto.getPostVisibility();
    }



    public void updateHotIssueScore(int score) {
        this.Score = score;
    }
}