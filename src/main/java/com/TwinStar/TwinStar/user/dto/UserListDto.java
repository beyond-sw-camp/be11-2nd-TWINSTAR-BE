package com.TwinStar.TwinStar.user.dto;

import com.TwinStar.TwinStar.user.domain.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class UserListDto {
    private Long id;
    private String email;
    private String nickName;
    private MultipartFile profileImg;//프론트에서 어떻게 정보를 줄거냐에 따라 타입 바뀔 수도
    private LocalDateTime createdTime;
    private UserStatus userStatus;
    private Long followerCount;//나를 팔로워한 사람의 수
    private Long followingCount;//내가 팔로워한 사람의 수
    private boolean isFollowing;//팔로우관계확인
}
