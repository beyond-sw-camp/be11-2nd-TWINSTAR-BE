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
public class ChatUserListDto {
    private Long id;
    private String nickName;
    private String profileImg;

    //    채팅용 유저목록 조회
    public ChatUserListDto(User user){
        this.id = user.getId();
        this.nickName = user.getNickName();
        this.profileImg = user.getProfileImg();
    }
}
