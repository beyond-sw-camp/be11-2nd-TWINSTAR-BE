package com.TwinStar.TwinStar.user.dto;

import com.TwinStar.TwinStar.common.domain.Visibility;
import com.TwinStar.TwinStar.user.domain.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
//회원 프로필 목록
public class UserProfileDto {
    private Long id;
    private String nickName;
    private String profileImg;
    private String profileTxt;
    private Long followerCount;//나를 팔로우하는 사람의 수
    private Long followingCount;//내가 팔로우하는 사람의 수
    private Visibility idVisibility;
    private UserStatus userStatus;
//    private List<PostfilePostResDto> posts;// 게시물 리스트

}
