package com.TwinStar.TwinStar.user.dto;

import com.TwinStar.TwinStar.user.domain.IdVisibility;
import com.TwinStar.TwinStar.user.domain.Sex;
import com.TwinStar.TwinStar.user.domain.User;
import com.TwinStar.TwinStar.user.domain.UserStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class UserSaveReq {
    @NotBlank
    private String email;
    @NotBlank(message = "이메일은 필수입니다.")
    private String password;
    @NotBlank(message = "비밀번호는 필수입니다.")
    private String nickName;
    @NotNull
    private Sex sex;
    @NotNull
    private IdVisibility idVisibility;
    @NotNull
    private UserStatus userStatus;

    private String profileImg;
    private String profileTxt;

    public User user(String encodedPassword){
        return User.builder()
                .email(this.email)
                .password(encodedPassword)
                .nickName(this.nickName)
                .sex(this.sex)
                .idVisibility(this.idVisibility)
                .userStatus(this.userStatus)
                .profileImg(this.profileImg)
                .profileTxt(this.profileTxt)
                .build();
    }
    public User toEntity(String encodedPassword) {
        return User.builder()
                .email(this.email)
                .password(encodedPassword)
                .nickName(this.nickName)
                .sex(this.sex)
                .idVisibility(this.idVisibility)
                .userStatus(this.userStatus)
                .profileImg(this.profileImg)
                .profileTxt(this.profileTxt)
                .build();
    }

}
