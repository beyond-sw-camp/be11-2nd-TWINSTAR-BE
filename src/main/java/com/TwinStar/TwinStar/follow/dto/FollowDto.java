package com.TwinStar.TwinStar.follow.dto;

import com.TwinStar.TwinStar.user.domain.User;
import lombok.Getter;
import software.amazon.awssdk.services.s3.endpoints.internal.Value;

import java.util.Objects;

@Getter
public class FollowDto {
    private Long id;
    private String nickName;
    private String profileImg;
    private String isFollow;

    public FollowDto(User user, String isFollow) {
        this.id = user.getId();
        this.nickName = user.getNickName();
        this.profileImg = user.getProfileImg();
        this.isFollow = isFollow;
    }

//    Set 또는 distinct()를 사용할 때, 중복된 FollowDto 객체를 제대로 제거하기 위해 equals()와 hashCode()를 함께 구현
    @Override
    public boolean equals(Object o) { // id값을 비교하여 객체가 같은지 확인 후 중복처리하기 위함
        if (this == o) return true;   // 'id값이 같으면 중복된 객체다' 라는걸 명시
        if (o == null || getClass() != o.getClass()) return false;
        FollowDto followDto = (FollowDto) o;
        return Objects.equals(id, followDto.id);
    }
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
