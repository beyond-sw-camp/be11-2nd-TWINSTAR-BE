package com.TwinStar.TwinStar.user.dto;

import com.TwinStar.TwinStar.common.domain.Visibility;
import com.TwinStar.TwinStar.common.domain.YN;
import com.TwinStar.TwinStar.user.domain.AdminYn;
import com.TwinStar.TwinStar.user.domain.Sex;
import com.TwinStar.TwinStar.user.domain.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
//회원의 모든 목록(관리자용으로 주로 쓰임)
public class UserListDto {
    private Long id;
    private String email;
    private String nickName;
    private Sex sex;
    private Visibility idVisibility;
    private UserStatus userStatus;
    private YN delYn;
    private AdminYn adminYn;
    private LocalDateTime createdAt;
}
