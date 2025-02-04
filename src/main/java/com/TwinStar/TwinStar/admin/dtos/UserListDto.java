package com.TwinStar.TwinStar.admin.dtos;

import com.TwinStar.TwinStar.user.domain.IdVisibility;
import com.TwinStar.TwinStar.user.domain.Sex;
import com.TwinStar.TwinStar.user.domain.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class UserListDto {
    private Long id;
    private String email;
    private String nickName;
    private String profileImg;//프론트에서 어떻게 정보를 줄거냐에 따라 타입 바뀔 수도
    private String profileTxt;
    private Sex sex;
    private IdVisibility idVisibility;
    private UserStatus userStatus;
    private String delYn;


}
