package com.TwinStar.TwinStar.follow.controller;

import com.TwinStar.TwinStar.common.auth.JwtUtil;
import com.TwinStar.TwinStar.follow.dto.FollowDto;
import com.TwinStar.TwinStar.user.domain.User;
import com.TwinStar.TwinStar.follow.service.FollowService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpHeaders;
import java.util.List;

@RequestMapping("/follow")
@RestController
public class FollowController {
    public final FollowService followService;
    public final JwtUtil jwtUtil;

    public FollowController(FollowService followService, JwtUtil jwtUtil) {
        this.followService = followService;
        this.jwtUtil = jwtUtil;
    }

//    토글 팔로우/언팔로우 요청
    @PostMapping("/toggle/{receiveUserId}")
    public ResponseEntity<String> toggleFollow(
            @PathVariable Long receiveUserId,@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader
    ) {
        String token = authorizationHeader.replace("Bearer ", "");
        // 토큰에서 userId 추출
        Long userId = jwtUtil.getUserId(token);
        boolean isFollowing = followService.toggleFollow(userId, receiveUserId);

        if (isFollowing) {
            return ResponseEntity.ok("팔로우 되었습니다.");
        } else {
            return ResponseEntity.ok("언팔로우 되었습니다.");
        }
    }

    // 특정 유저의 팔로워 수 조회 (공개 API)
    @GetMapping("/count/userId/{userId}")
    public ResponseEntity<Long> countFollowersByUserId(@PathVariable Long userId) {
        long count = followService.countByUserIdAndFollowYn(userId);
        return ResponseEntity.ok(count);
    }

    // 특정 유저의 팔로잉 수 조회 (공개 API)
    @GetMapping("/count/receiveUserId/{userId}")
    public ResponseEntity<Long> countFollowingByUserId(@PathVariable Long userId) {
        long count = followService.countByReceiveUserIdAndFollowYn(userId);
        return ResponseEntity.ok(count);
    }

//        팔로워 목록 조회
    @GetMapping("/list/userId")
    public ResponseEntity<List<FollowDto>> getFollowerList(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader) {
        String token = authorizationHeader.replace("Bearer ", "");
        Long userId = jwtUtil.getUserId(token);
        return ResponseEntity.ok(followService.getFollowerList(userId));
    }

//        팔로잉 목록 조회
    @GetMapping("/list/receiveUserId")
    public ResponseEntity<List<FollowDto>> getFollowingList(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader) {
        String token = authorizationHeader.replace("Bearer ", "");
        Long userId = jwtUtil.getUserId(token);
        return ResponseEntity.ok(followService.getFollowingList(userId));
    }
}
