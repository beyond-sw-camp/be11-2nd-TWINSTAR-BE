package com.TwinStar.TwinStar.user.domain;

import com.TwinStar.TwinStar.common.domain.BaseTimeEntity;
import com.TwinStar.TwinStar.common.domain.YN;
import com.TwinStar.TwinStar.post.domain.Post;
import com.TwinStar.TwinStar.report.domain.Report;
import com.TwinStar.TwinStar.user.dto.UserListDto;
import com.TwinStar.TwinStar.user.dto.UserProfileDto;
import com.TwinStar.TwinStar.user.dto.UserProfileUpdateDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
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
    @Column(nullable = false, unique = true, name = "email")
    private String email;
    @Column(nullable = false, unique = true)
    private String nickName;
    private String profileImg;
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
    private AdminYn adminYn = AdminYn.USER;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UserStatus userStatus;
    @OneToMany(mappedBy = "user", cascade = CascadeType.PERSIST)//자동저장/ 삭제는 메소드 사용
    @Builder.Default //회원가입하면 게시물이 0개
    private List<Post> posts = new ArrayList<>();

    @OneToMany(mappedBy = "userId", cascade = CascadeType.PERSIST)
    @Builder.Default
    private List<Report> reports = new ArrayList<>();

//    @OneToMany(mappedBy = "follower", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<Follow> following = new ArrayList<>();
//
//    @OneToMany(mappedBy = "following", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<Follow> followers = new ArrayList<>();

    //    관리자용 유저 목록조회
    public UserListDto listFromEntity() {
        return UserListDto.builder()
                .id(this.id)
                .email(this.email)
                .password(this.password)
                .nickName(this.nickName)
                .profileImg(this.profileImg)
                .profileTxt(this.profileTxt)
                .sex(this.sex)
                .idVisibility(this.idVisibility)
                .userStatus(this.userStatus)
                .adminYn(this.adminYn)
                .delYn(this.delYn)
                .build();
    }

//    //    프로필 조회 엔티티
//    public UserProfileDto detailFromEntity(Long followerCount, Long followingCount, List<PostfilePostResDto> posts) {
//        return UserProfileDto.builder()
//                .id(this.id)
//                .nickName(this.nickName)
//                .profileImg(this.profileImg)
//                .profileTxt(this.profileTxt)
//                .followerCount(followerCount)
//                .followingCount(followingCount)
//                .idVisibility(this.idVisibility)
//                .userStatus(this.userStatus)
//                .posts(posts)
//                .build();
//    }

    //    사용자 프로필 업데이트
    public void updateProfile(String nickName, String profileTxt, Sex sex, IdVisibility idVisibility) {
        UserProfileUpdateDto.builder()
                .nickName(this.nickName)
                .profileTxt(this.profileTxt)
                .sex(this.sex)
                .idVisibility(this.idVisibility)
                .build();
    }

    //    프로필 이미지 변경
    public void updateProfileImage(String profileImgUrl) {
        this.profileImg = profileImgUrl;
    }

    //소프트 딜리트메서드 추가
    public void deleteUser() {
        this.delYn = YN.valueOf("Y");
    }

    //    비밀번호 변경
    public void changePassword(String newPassword, PasswordEncoder passwordEncoder) {
        this.password = passwordEncoder.encode(newPassword);
    }


    //    상태 변경을 위한 메서드
    public void changeStatus(IdVisibility newIdVisibility){
        if (this.idVisibility == newIdVisibility){
            throw new IllegalStateException("이미 현재상태와 동일합니다.");
        }
        this.idVisibility = newIdVisibility;
    }

    // 관리자 권한 변경 메서드
    public void changeAdmin(AdminYn newRole){
        this.adminYn = newRole;
    }
}
