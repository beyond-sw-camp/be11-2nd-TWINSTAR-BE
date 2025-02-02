package com.TwinStar.TwinStar.commonDomain;

import com.TwinStar.TwinStar.admin.dtos.AdminPostListDto;
import com.TwinStar.TwinStar.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@Entity
@NoArgsConstructor
@Getter
@Transactional
@AllArgsConstructor
@Builder
@Table(name = "post")
public class Post extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "group_id")
//    private Group groupId; //group테이블 생성되면 활성화
    @Column(columnDefinition = "VARCHAR(3000)")
    private String content;
    @Column(nullable = false)
    private String updateStatus;
    @Column(nullable = false)
    private Long sharePostId;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PostVisibility postVisibility;
    @Column(name = "post_del",nullable = false)
    private String postDel;
    private Long score;
    @Column(name = "hot_issue_yn", nullable = false)
    private String hotIssueYn;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PostStatus postStatus;

    @PrePersist //null값이 들어올 경우 자동으로 기본값 설정
    public void prePersist(){
        if (postDel == null){
            postDel = "N";
        } else if (hotIssueYn == null) {
            hotIssueYn = "Y";
        } else if (updateStatus == null) {
            updateStatus = "N";
        }
    }

    public AdminPostListDto listFromEntity(){
        return AdminPostListDto.builder()
                .id(this.id)
                .nickName(nickName)
                .postVisibility(this.postVisibility)
                .postDel(this.postDel)
                .postStatus(this.postStatus)
                .sharePostId(this.sharePostId)
                .content(this.content)
                .hotIssueYn(this.hotIssueYn)
                .build();
    }

//    public AdminPostListDto toDetailDto(String nickName,Long groupId){
//        return AdminPostListDto.builder()
//                .id(this.id)
//                .nickName(nickName)
//                .postVisibility(this.postVisibility)
//                .postDel(this.postDel)
//                .postStatus(this.postStatus)
//                .sharePostId(this.sharePostId)
//                .content(this.content)
//                .hotIssueYn(this.hotIssueYn)
//                .build();
//    }
}
