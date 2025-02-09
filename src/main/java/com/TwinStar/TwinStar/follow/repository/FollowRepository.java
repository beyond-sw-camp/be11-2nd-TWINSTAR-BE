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

    boolean existsByFollowerAndFollowing(User follower, User following);
    void deleteByFollowerAndFollowing(User follower, User following);
    long countByFollowing(User following); // 팔로워 수
    long countByFollower(User follower);   // 팔로잉 수
    List<Follow> findByFollower(User follower); // 내가 팔로우한 목록
    List<Follow> findByFollowing(User following); // 나를 팔로우한 목록


}
