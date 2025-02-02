package com.TwinStar.TwinStar.admin.dtos;

import com.TwinStar.TwinStar.commonDomain.IdVisibility;
import com.TwinStar.TwinStar.commonDomain.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserDetailDto {
    private String email;
    private String profileImg;//프론트에서 어떻게 정보를 줄거냐에 따라 타입 바뀔 수도
    private String profileTxt;
    private IdVisibility idVisibility;
    private UserStatus userStatus;
    private String delYn;
}
