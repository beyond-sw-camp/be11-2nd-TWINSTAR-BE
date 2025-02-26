package com.TwinStar.TwinStar.user.dto;

import com.TwinStar.TwinStar.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class UserListResDto {
    private Long id;
    private String nickName;
    private String profileImg;
    private String isFollow;

    public UserListResDto toUserListResDto(User user, String isFollow){
        return UserListResDto.builder()
                .id(user.getId())
                .nickName(user.getNickName())
                .profileImg(user.getProfileImg())
                .isFollow(isFollow)
                .build();
    }
}
