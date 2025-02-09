package com.TwinStar.TwinStar.follow.repository;


import com.TwinStar.TwinStar.common.domain.YN;
import com.TwinStar.TwinStar.follow.domain.Follow;
import com.TwinStar.TwinStar.post.domain.Post;
import com.TwinStar.TwinStar.user.domain.User;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface FollowRepository extends JpaRepository<Follow, Long> {
    //    특정 Follower와 Following 간의 관계를 조회하는 메서드 *toggleFollow에서 사용
    Optional<Follow> findByUserIdAndReceiveUserId(User userId, User receiveUserId);
    //
//    boolean existsByUserIdAndReceiveUserId(User userId, User receiveUserId);
    boolean existsByFollowerAndFollowing(User follower, User following);
    //     팔로워 수
    Long countByReceiveUserIdAndFollowYn(User receiveUserId, YN followYn);

    // 팔로잉 수 조회 (내가 팔로우한 사람)
    Long countByUserIdAndFollowYn(User userId, YN followYn);

    //     나를 팔로우한 목록
    List<Follow> findByUserIdAndFollowYn(User userId, YN followYn);

    //         내가 팔로우한 목록
    List<Follow> findByReceiveUserIdAndFollowYn(User receiveUserId, YN followYn);
}
