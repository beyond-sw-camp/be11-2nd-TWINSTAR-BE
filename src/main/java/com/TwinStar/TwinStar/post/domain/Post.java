package com.TwinStar.TwinStar.post.domain;

import com.TwinStar.TwinStar.comment.domain.Comment;
import com.TwinStar.TwinStar.common.domain.BaseTimeEntity;
import com.TwinStar.TwinStar.common.domain.Visibility;
import com.TwinStar.TwinStar.post.dto.PostUpdateReqDto;
import com.TwinStar.TwinStar.post_file.PostFile;
import com.TwinStar.TwinStar.post_file.PostFileService;
import com.TwinStar.TwinStar.user.domain.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
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

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private Visibility postVisibility = Visibility.ALL;

    @Column(nullable = false, length = 1)
    @Builder.Default
    private String postDel = "N"; // "Y" 또는 "N" 값으로 변경

    @Column(nullable = false, length = 1)
    @Builder.Default
    private String hotIssueYn = "Y"; // "Y" 또는 "N"

    @Column(nullable = false)
    @Builder.Default
    private String postStatus = "ACTIVE"; // 기본값 설정

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<PostFile> postFiles = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.PERSIST)
    @Builder.Default
    private List<Comment> comments = new ArrayList<>();

    @Column(nullable = false)
    @Builder.Default
    private int score = 0;

    public void update(PostUpdateReqDto dto, PostFileService postFileService) {
        this.content = dto.getContent();
        this.postFiles.clear();
        this.postFiles.addAll(postFileService.convertToPostFiles(this, dto.getPostFileUrls()));
        this.postVisibility = dto.getPostVisibility();
    }

    // ✅ 게시글 삭제 처리 메서드 추가
    public void updatePostDel(String postDel) {
        this.postDel = postDel;
    }

    // ✅ 공개 범위 변경 메서드 추가
    public void updatePostVisibility(Visibility visibility) {
        this.postVisibility = visibility;
    }
}
