package com.TwinStar.TwinStar.follow.repository;


import com.TwinStar.TwinStar.common.domain.YN;
import com.TwinStar.TwinStar.follow.domain.Follow;
import com.TwinStar.TwinStar.post.domain.Post;
import com.TwinStar.TwinStar.user.domain.User;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface FollowRepository extends JpaRepository<Follow, Long> {
    //    특정 Follower와 Following 간의 관계를 조회하는 메서드 *toggleFollow에서 사용
    Optional<Follow> findByUserIdAndReceiveUserId(User userId, User receiveUserId);
    //
    boolean existsByUserIdAndReceiveUserId(User userId, User receiveUserId);
    //     팔로워 수
    Long countByReceiveUserIdAndFollowYn(User receiveUserId, YN followYn);

    // 팔로잉 수 조회 (내가 팔로우한 사람)
    Long countByUserIdAndFollowYn(User userId, YN followYn);

    // 나를 팔로우한 유저 목록 (페이징 적용)
    Page<Follow> findByReceiveUserIdAndFollowYn(User receiveUserId, YN followYn, Pageable pageable);

    // 내가 팔로우한 유저 목록 (페이징 적용)
    Page<Follow> findByUserIdAndFollowYn(User receiveUserId, YN followYn, Pageable pageable);
//    //     나를 팔로우한 목록
//    List<Follow> findByUserIdAndFollowYn(User userId, YN followYn);
//
//    //         내가 팔로우한 목록
//    List<Follow> findByReceiveUserIdAndFollowYn(User receiveUserId, YN followYn);

    // 내가 팔로우한 유저 ID 조회
    @Query("SELECT f.receiveUserId.id FROM Follow f WHERE f.userId.id = :userId AND f.followYn = 'Y'")
    List<Long> findFollowingUserIds(@Param("userId") Long userId);

    // 나와 맞팔로우 관계인 유저 ID 조회
    @Query("""
        SELECT f.receiveUserId.id FROM Follow f 
        WHERE f.userId.id = :userId AND f.followYn = 'Y'
        AND f.receiveUserId.id IN (
            SELECT f2.userId.id FROM Follow f2 
            WHERE f2.receiveUserId.id = :userId AND f2.followYn = 'Y'
        )
    """)
    List<Long> findMutualFollowUserIds(@Param("userId") Long userId);

    Boolean existsByUserIdAndReceiveUserIdAndFollowYn(User user, User receiveUser, YN followYn);

}
