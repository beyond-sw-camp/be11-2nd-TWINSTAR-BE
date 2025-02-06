package com.TwinStar.TwinStar.post.domain;

import com.TwinStar.TwinStar.common.domain.BaseTimeEntity;
import com.TwinStar.TwinStar.common.domain.YN;
import com.TwinStar.TwinStar.post.dto.PostUpdateReqDto;
import com.TwinStar.TwinStar.user.domain.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity
public class Post extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "content", length = 3000)
    private String content;

    @Column(name = "update_status", nullable = false)
    private YN updatedStatus = YN.N;

    @Column(name = "share_post_id", nullable = false)
    private Long sharePostId = 0L;

    @Column(name = "post_visibility", nullable = false)
    PostVisibility visibility;

    @Column(name = "post_del", nullable = false)
    private YN postDel = YN.N;

    @Column(name = "like_count")
    private Long likeCount = 0L;

    @Column(name = "hot_issue_yn", nullable = false)
    private YN hotIssueYn = YN.Y;

    private String postFileUrl;

    private String profileImgUrl;

    @PrePersist
    public void prePersist() {
        if (this.hotIssueYn == null) {
            this.hotIssueYn = YN.Y; // 기본값 설정
        }
        if (this.postDel == null) {
            this.postDel = YN.N; // 기본값 설정
        }
        if (this.updatedStatus == null) {
            this.updatedStatus = YN.N; // 기본값 설정
        }
        if (this.sharePostId == null) {
            this.sharePostId = 0L; // 기본값 설정
        }
        if (this.likeCount == null) {
            this.likeCount = 0L; // 기본값 설정
        }
    }


    public Post update(PostUpdateReqDto dto){
        this.content = dto.getContent();
        this.updatedStatus = YN.Y;
        this.postFileUrl = dto.toEntity().getPostFileUrl();
        this.profileImgUrl = dto.getProfileImgUrl();
        this.visibility = dto.getVisibility();
        return this;
    }
}