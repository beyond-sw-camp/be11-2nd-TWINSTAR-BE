package com.TwinStar.TwinStar.user.dto;

import com.TwinStar.TwinStar.user.domain.User;
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
public class UserDetailDto {
    private Long id;
    private String nickName;
    private LocalDateTime banCloseTime; // 정지 해제 날짜
    private UserStatus userStatus;//정지인지 아닌지 확인

    public static UserDetailDto detailList(User user){
        return UserDetailDto.builder()
                .id(user.getId())
                .nickName(user.getNickName())
                .banCloseTime(user.getBanCloseTime())
                .userStatus(user.getUserStatus())
                .build();
    }

    
}
