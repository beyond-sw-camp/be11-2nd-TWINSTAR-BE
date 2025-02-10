package com.TwinStar.TwinStar.user.dto;

import com.TwinStar.TwinStar.common.domain.Visibility;
import com.TwinStar.TwinStar.user.domain.Sex;
import com.TwinStar.TwinStar.user.domain.User;
import com.TwinStar.TwinStar.user.domain.UserStatus;
import jakarta.validation.constraints.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class UserSaveReq {
    @NotBlank(message = "이메일은 필수입니다.")
    @Email(message = "올바른 이메일 형식이어야 합니다.")
    private String email;
    @NotBlank(message = "비밀번호는 필수입니다.")
    @Size(min = 8,message = "비밀번호는 최소 8자 이상이어야 합니다.")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d!@#$%^&*()_+]{8,}$",
            message = "비밀번호는 최소 8자 이상이며, 숫자와 문자를 포함해야 합니다.")
    private String password;
    @NotBlank
    private String nickName;
    @NotNull
    private Sex sex;
    @NotNull
    private Visibility idVisibility;
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
