package com.TwinStar.TwinStar.user.dto;

import com.TwinStar.TwinStar.common.domain.YN;
import com.TwinStar.TwinStar.user.domain.IdVisibility;
import com.TwinStar.TwinStar.user.domain.Sex;
import com.TwinStar.TwinStar.user.domain.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
//회원의 모든 목록(관리자용으로 주로 쓰임)
public class UserListDto {
    private Long id;
    private String email;
    private String password;
    private String nickName;
    private String profileImg;
    private String profileTxt;
    private Sex sex;
    private IdVisibility idVisibility;
    private UserStatus userStatus;
    private YN delYn;
    private YN adminYn;
}
