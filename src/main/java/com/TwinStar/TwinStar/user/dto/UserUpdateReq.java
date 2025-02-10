package com.TwinStar.TwinStar.user.dto;

import com.TwinStar.TwinStar.common.domain.Visibility;
import com.TwinStar.TwinStar.user.domain.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class UserUpdateReq {
    private String password;
    private String nickName;
    private String profileImg;
    private String profileTxt;
    private Visibility idVisibility;
    private UserStatus userStatus;

//    UserUpdateReq에서 값이 있는 필드만 변경
    public void updateUser(UserUpdateReq req) {
        if (req.getPassword() != null) {
            this.password = req.getPassword();
        }
        if (req.getNickName() != null) {
            this.nickName = req.getNickName();
        }
        if (req.getProfileImg() != null) {
            this.profileImg = req.getProfileImg();
        }
        if (req.getProfileTxt() != null) {
            this.profileTxt = req.getProfileTxt();
        }
        if (req.getIdVisibility() != null) {
            this.idVisibility = req.getIdVisibility();
        }
        if (req.getUserStatus() != null) {
            this.userStatus = req.getUserStatus();
        }
    }


}
