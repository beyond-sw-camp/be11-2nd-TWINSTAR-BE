package com.TwinStar.TwinStar.commonDomain;

import com.TwinStar.TwinStar.admin.dtos.UserDetailDto;
import com.TwinStar.TwinStar.admin.dtos.UserListDto;
import com.TwinStar.TwinStar.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
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

    @Column(nullable = false, unique = true)
    private String email;
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
    @Builder.Default //posts에 값이 지정되지 않으면 기본값인 기본생성자가 적용됨.
//    Default값을 사용하지 않으면 기본값이 유지되지 않고, null또는 0으로 초기화됨
    private List<Post> posts = new ArrayList<>();
    @OneToMany(mappedBy = "userId", cascade = CascadeType.PERSIST)
    @Builder.Default //
    private List<Report> reports = new ArrayList<>();

    public UserListDto listFromEntity(){
        return UserListDto.builder()
                .id(this.id)
                .email(this.email)
                .nickName(this.nickName)
                .sex(this.sex)
                .build();
    }
    public UserDetailDto detailFromEntity(){
        return new UserDetailDto(this.email,this.profileImg,this.profileTxt,this.idVisibility,this.userStatus,this.delYn);
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
