package com.TwinStar.TwinStar.user.service;


import com.TwinStar.TwinStar.user.domain.Follow;
import com.TwinStar.TwinStar.user.domain.User;
import com.TwinStar.TwinStar.user.dtos.FollowReq;
import com.TwinStar.TwinStar.user.repository.FollowRepository;
import com.TwinStar.TwinStar.user.repository.UserRepository;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class FollowService extends FollowReq {
    private final FollowRepository followRepository;
    private final UserRepository userRepository;

    public FollowService(FollowRepository followRepository, UserRepository userRepository) {
        this.followRepository = followRepository;
        this.userRepository = userRepository;
    }
//    팔로우 요청
    public void follow(Long followerId, Long followingId) {
        User follower = userRepository.findById(followerId)
                .orElseThrow(() -> new RuntimeException("유저를 찾을수 없습니다"));
        User following = userRepository.findById(followingId)
                .orElseThrow(() -> new RuntimeException("유저를 찾을수 없습니다"));

        Follow follow = new Follow(follower, following);
        followRepository.save(follow);
    }


//     언팔로우 요청
    public void unfollow(Long followerId, Long followingId) {
        User follower = userRepository.findById(followerId)
                .orElseThrow(() -> new IllegalArgumentException("팔로워를 찾을 수 없습니다."));
        User following = userRepository.findById(followingId)
                .orElseThrow(() -> new IllegalArgumentException("팔로잉할 사용자를 찾을 수 없습니다."));

        if (!followRepository.existsByFollowerAndFollowing(follower, following)) {
            throw new IllegalStateException("팔로우 관계가 존재하지 않습니다.");
        }

        followRepository.deleteByFollowerAndFollowing(follower, following);
    }

    // 팔로워 수 조회
    public long getFollowerCount(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다."));
        return followRepository.countByFollowing(user);
    }

    // 팔로잉 수 조회
    public long getFollowingCount(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다."));
        return followRepository.countByFollower(user);
    }


    // 내가 팔로우한 유저 목록
    public List<User> getFollowingList(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("유저가 존재하지 않습니다."));
        return followRepository.findByFollower(user)
                .stream().map(Follow::getFollowing)
                .collect(Collectors.toList());
    }

    // 나를 팔로우한 유저 목록
    public List<User> getFollowerList(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("유저가 존재하지 않습니다."));
        return followRepository.findByFollowing(user)
                .stream().map(Follow::getFollower)
                .collect(Collectors.toList());
    }
}




