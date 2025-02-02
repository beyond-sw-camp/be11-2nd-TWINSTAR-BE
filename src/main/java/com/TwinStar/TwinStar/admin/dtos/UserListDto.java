package com.TwinStar.TwinStar.admin.dtos;

import com.TwinStar.TwinStar.commonDomain.IdVisibility;
import com.TwinStar.TwinStar.commonDomain.Sex;
import com.TwinStar.TwinStar.commonDomain.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Blob;
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
