package com.TwinStar.TwinStar.user.domain;

import com.TwinStar.TwinStar.common.domain.BaseTimeEntity;
import com.TwinStar.TwinStar.common.domain.YN;
import com.TwinStar.TwinStar.follow.domain.Follow;
import com.TwinStar.TwinStar.post.domain.Post;
import com.TwinStar.TwinStar.report.domain.Report;
import com.TwinStar.TwinStar.user.dto.UserListDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user")
@Transactional
@Builder
public class User extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false, unique = true)
    private String email;
    @Column(nullable = false, unique = true)
    private String nickName;
    private MultipartFile profileImg;
    private String profileTxt;
    @Column(nullable = false)
    @Builder.Default
    private YN delYn = YN.valueOf("N");
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Sex sex;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private IdVisibility idVisibility;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private YN adminYn = YN.valueOf("N");
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UserStatus userStatus;
    @OneToMany(mappedBy = "user", cascade = CascadeType.PERSIST)//자동저장/ 삭제는 메소드 사용
    @Builder.Default //회원가입하면 게시물이 0개
    private List<Post> posts = new ArrayList<>();

    @OneToMany(mappedBy = "userId", cascade = CascadeType.PERSIST)
    @Builder.Default
    private List<Report> reports = new ArrayList<>();

    @OneToMany(mappedBy = "follower", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Follow> following = new ArrayList<>();

    @OneToMany(mappedBy = "following", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Follow> followers = new ArrayList<>();

    public UserListDto listFromEntity(User loginUser){
        boolean isFollowing = loginUser != null && this.followers.stream()
                .anyMatch(follow -> follow.getFollower().getId().equals(loginUser.getId()));

        return UserListDto.builder()
                .id(this.id)
                .email(this.email)
                .nickName(this.nickName)
                .profileImg(this.profileImg)
                .profileTxt(this.profileTxt)
                .idVisibility(this.idVisibility)
                .userStatus(this.userStatus)
                .follower
                .build();
    }

}
