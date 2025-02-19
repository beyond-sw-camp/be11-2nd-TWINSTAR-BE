package com.TwinStar.TwinStar.user.dto;

import com.TwinStar.TwinStar.common.domain.Visibility;
import com.TwinStar.TwinStar.post.domain.Post;
import com.TwinStar.TwinStar.post.dto.PostResponseDto;
import com.TwinStar.TwinStar.post.dto.ProfilePostResDto;
import com.TwinStar.TwinStar.post_file.PostFile;
import com.TwinStar.TwinStar.user.domain.User;
import com.TwinStar.TwinStar.user.domain.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

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
    private List<PostResponseDto> posts;// 게시물 리스트

    public static UserProfileDto profileSearch(User user,Long followerCount,Long followingCount){
        return UserProfileDto.builder()
                .id(user.getId())
                .nickName(user.getNickName())
                .profileImg(user.getProfileImg())
                .profileTxt(user.getProfileTxt())
                .followerCount(followerCount)
                .followingCount(followingCount)
                .idVisibility(user.getIdVisibility())
                .userStatus(user.getUserStatus())
                .posts(user.getPosts().stream()
                        .filter(post -> !post.getPostDel().equals("Y")) // 삭제된 게시물 제외
                        .map(PostResponseDto::fromEntity) //
                        .collect(Collectors.toList()))
                .build();
    }

}
