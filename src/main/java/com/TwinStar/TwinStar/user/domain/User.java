package com.TwinStar.TwinStar.user.domain;

import com.TwinStar.TwinStar.admin.dtos.UserListDto;
<<<<<<< HEAD
import com.TwinStar.TwinStar.common.domain.BaseTimeEntity;
=======
import com.TwinStar.TwinStar.common.BaseTimeEntity;
>>>>>>> f7f29ab564e54211eb9b427a74d409bdae31f180
import com.TwinStar.TwinStar.follow.domain.Follow;
import com.TwinStar.TwinStar.post.domain.Post;
import com.TwinStar.TwinStar.report.domain.Report;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
<<<<<<< HEAD
import java.util.List;
=======
>>>>>>> f7f29ab564e54211eb9b427a74d409bdae31f180

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user")
@Transactional
@Builder
public class User extends BaseTimeEntity {
<<<<<<< HEAD
=======

>>>>>>> f7f29ab564e54211eb9b427a74d409bdae31f180
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String email;
<<<<<<< HEAD
    @Column(nullable = false, unique = true)
    private String nickName;
    private String profileImg;
    private String profileTxt;
    @Column(nullable = false)
    private String delYn;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Sex sex;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private IdVisibility idVisibility;
    @Column(nullable = false)
    private String adminYn;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UserStatus userStatus;
    @OneToMany(mappedBy = "user", cascade = CascadeType.PERSIST)//자동저장/ 삭제는 메소드 사용
    @Builder.Default //회원가입하면 게시물이 0개
    private List<Post> posts = new ArrayList<>();
=======

    @Column(nullable = false, unique = true)
    private String nickName;

    private String profileImg;

    private String profileTxt;

    @Column(nullable = false)
    private String delYn;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Sex sex;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private IdVisibility idVisibility;

    @Column(nullable = false)
    private String adminYn;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UserStatus userStatus;

    @OneToMany(mappedBy = "user", cascade = CascadeType.PERSIST)//자동저장/ 삭제는 메소드 사용
    @Builder.Default //회원가입하면 게시물이 0개
    private List<Post> posts = new ArrayList<>();

>>>>>>> f7f29ab564e54211eb9b427a74d409bdae31f180
    @OneToMany(mappedBy = "userId", cascade = CascadeType.PERSIST)
    @Builder.Default
    private List<Report> reports = new ArrayList<>();

<<<<<<< HEAD
=======
    @OneToMany(mappedBy = "follower", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Follow> following = new ArrayList<>();

    @OneToMany(mappedBy = "following", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Follow> followers = new ArrayList<>();

>>>>>>> f7f29ab564e54211eb9b427a74d409bdae31f180
    public UserListDto listFromEntity(){
        return UserListDto.builder()
                .id(this.id)
                .email(this.email)
                .nickName(this.nickName)
                .profileImg(this.profileImg)
                .profileTxt(this.profileTxt)
                .sex(this.sex)
                .idVisibility(this.idVisibility)
                .userStatus(this.userStatus)
                .build();
    }
    @PrePersist
    public void prePersist(){
        if (delYn == null){
            delYn = "N";
        } else if (adminYn == null) {
            adminYn = "N";
        } else if (userStatus == null) {
            userStatus = UserStatus.ACTIVE;
        }
    }
}
