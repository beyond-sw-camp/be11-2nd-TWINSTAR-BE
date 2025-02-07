package com.TwinStar.TwinStar.user.dto;

import com.TwinStar.TwinStar.user.domain.IdVisibility;
import com.TwinStar.TwinStar.user.domain.Sex;
import com.TwinStar.TwinStar.user.domain.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserProfileDto {
    private Long id;
    private String nickName;
    private String profileImg;
    private String profileTxt;
    private Sex sex;
    private Long postCount;//게시물 수
    private Long followerCount;//나를 팔로우하는 사람의 수
    private Long followingCount;//내가 팔로우하는 사람의 수
    private IdVisibility idVisibility;
    private UserStatus userStatus;

}
