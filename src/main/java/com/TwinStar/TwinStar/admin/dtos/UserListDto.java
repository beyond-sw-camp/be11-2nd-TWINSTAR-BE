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
    private Sex sex;


}
