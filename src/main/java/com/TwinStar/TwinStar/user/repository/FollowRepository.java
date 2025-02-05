package com.TwinStar.TwinStar.user.repository;


import com.TwinStar.TwinStar.user.domain.Follow;
import com.TwinStar.TwinStar.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FollowRepository extends JpaRepository<Follow, Long> {

//    팔로우 관계 조회
    Optional<Follow> findByFollowerIdAndFollowingId(Long followerId, Long followingId);

//    팔로워 목록 조회
    List<User> findFollowersByUserId(Long userId);

//    팔로잉 목록 조회
    List<User> findFollowingByUserId(Long userId);

    boolean existsByFollowerAndFollowing(User follower, User following);
    void deleteByFollowerAndFollowing(User follower, User following);
    long countByFollowing(User following); // 팔로워 수
    long countByFollower(User follower);   // 팔로잉 수
    List<Follow> findByFollower(User follower); // 내가 팔로우한 목록
    List<Follow> findByFollowing(User following); // 나를 팔로우한 목록


}
