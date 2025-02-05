package com.TwinStar.TwinStar.follow.controller;

import com.TwinStar.TwinStar.user.domain.User;
import com.TwinStar.TwinStar.follow.service.FollowService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class FollowController {
    public final FollowService followService;

    public FollowController(FollowService followService) {
        this.followService = followService;
    }


    //    팔로우
    @PostMapping("/{followerId}/follow/{followingId}")
    public ResponseEntity<String> follow(@PathVariable Long followerId, @PathVariable Long followingId) {
        followService.follow(followerId, followingId);
        return ResponseEntity.ok(followerId + "님이 " + followingId + "님을 팔로우하였습니다.");
    }


    // 언팔로우
    @DeleteMapping("/{followerId}/unfollow/{followingId}")
    public ResponseEntity<String> unfollow(@PathVariable Long followerId, @PathVariable Long followingId) {
        followService.unfollow(followerId, followingId);
        return ResponseEntity.ok(followerId + "님이 " + followingId + "님을 언팔로우하였습니다.");
    }

    //    팔로워 목록 조회
    @GetMapping("/{userId}/followers")
    public ResponseEntity<List<User>> getFollowers(@PathVariable Long userId) {
        List<User> followers = followService.getFollowingList(userId);
        return ResponseEntity.ok(followService.getFollowingList(userId));
    }
    //    팔로잉 목록 조회
    @GetMapping("/{userId}/following")
    public ResponseEntity<List<User>> getFollowing(@PathVariable Long userId){
        return ResponseEntity.ok(followService.getFollowerList(userId));



    }
}
