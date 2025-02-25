package com.TwinStar.TwinStar.comment.dto;

import com.TwinStar.TwinStar.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CommentUserListDto {
    private Long id;
    private String nickName;
    private String profileImg;

    public CommentUserListDto(User user){
        this.id = user.getId();
        this.nickName = user.getNickName();
        this.profileImg = user.getProfileImg();
    }
}
