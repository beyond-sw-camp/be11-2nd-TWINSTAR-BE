package com.TwinStar.TwinStar.post.domain;

import jakarta.persistence.*;
import jakarta.transaction.Transactional;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@ToString
@Entity
@Transactional
public class Post extends BaseTimeEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 3000)
    private String contents;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "group_id")
    private Long groupId;

    @Column(name = "share_post_id")
    private Long sharePostId;

    @Column(name = "post_visibility")
    @Enumerated(EnumType.STRING)
    private Enum postVisibility;

    @Column(name = "post_del")
    private int postDel;

    @Column(name = "score")
    private Long score;

    @Column(name = "hot_issue_yn")
    private int hotIssueYn;

    public PostListRes postListFromEntity(){
        return PostListRes.builder()
                .id(this.id)
                .userNickname(this.user.getNickname())
                .build();
    }

    public PostDetailRes detailFromEntity(){
        return PostDetailRes.builder()
                .id(this.id)
                .contents(this.contents)
                .userNickname(this.user.getNickname())
                .createdTime(this.getCreatedTime())
                .updatedTime(this.getUpdatedTime())
                .build();
    }
}
