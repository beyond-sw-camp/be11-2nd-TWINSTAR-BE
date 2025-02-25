package com.TwinStar.TwinStar.follow.service;


import com.TwinStar.TwinStar.alarm.service.AlarmService;
import com.TwinStar.TwinStar.common.domain.YN;
import com.TwinStar.TwinStar.follow.domain.Follow;
import com.TwinStar.TwinStar.follow.dto.FollowDto;
import com.TwinStar.TwinStar.user.domain.User;
import com.TwinStar.TwinStar.follow.repository.FollowRepository;
import com.TwinStar.TwinStar.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class FollowService {
    private final FollowRepository followRepository;
    private final UserRepository userRepository;
    private final AlarmService alarmService;

    public FollowService(FollowRepository followRepository, UserRepository userRepository, AlarmService alarmService) {
        this.followRepository = followRepository;
        this.userRepository = userRepository;
        this.alarmService = alarmService;
    }

    //    토글 팔로우/언팔로우 요청
    @Transactional
    public boolean toggleFollow(Long userId, Long receiveUserId) {
        User followRequest = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("팔로워가 존재하지 않습니다."));
        User receiveFollowRequest = userRepository.findById(receiveUserId)
                .orElseThrow(() -> new IllegalArgumentException("팔로잉 대상이 존재하지 않습니다."));

        // 팔로우 상태 확인
        Optional<Follow> existingFollow = followRepository.findByUserIdAndReceiveUserId(followRequest,receiveFollowRequest);

        String content= followRequest.getNickName()+"님이 회원님에게 팔로우 요청을 보냈습니다.";
        String url = "http://localhost:3000/user/detail/"+followRequest.getId();
        alarmService.createAlarm(followRequest,content,url);

        if (existingFollow.isPresent()) {
            Follow follow = existingFollow.get();
            follow.toggleFollow();  // followYn 값 변경 (Y <-> N)
            followRepository.save(follow);
            return follow.getFollowYn() == YN.Y; // true = 팔로우 상태, false = 언팔로우 상태
        } else {
            // 팔로우하고 있지 않다면 새로 팔로우
            Follow follow = new Follow(followRequest, receiveFollowRequest);
            followRepository.save(follow);
            return true; // 팔로우했으므로 true 반환
        }

    }

    // 팔로워 수 조회
    public Long countByUserIdAndFollowYn(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다."));
        return Optional.ofNullable(followRepository.countByReceiveUserIdAndFollowYn(user, YN.Y))
                .orElse(0L); //nullpointexception방지 위한 0값 설정
    }

    // 팔로잉 수 조회
    public Long countByReceiveUserIdAndFollowYn(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다."));
//        return Optional.ofNullable(followRepository.countByUserIdAndFollowYn(user))
//                .orElse(0L);
        return Optional.ofNullable(followRepository.countByUserIdAndFollowYn(user, YN.Y))
                .orElse(0L);
    }

    // 나를 팔로우한 유저 목록
    public List<FollowDto> getFollowerList(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("유저가 존재하지 않습니다."));
        return followRepository.findByReceiveUserIdAndFollowYn(user, YN.Y)
                .stream().map(follow -> new FollowDto(follow.getUserId()))
                .collect(Collectors.toSet()) // 중복 제거 (Set 사용)
                .stream().toList();
    }

    // 내가 팔로우한 유저 목록
    public List<FollowDto> getFollowingList(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("유저가 존재하지 않습니다."));
        return followRepository.findByUserIdAndFollowYn(user, YN.Y)
                .stream().map(follow -> new FollowDto(follow.getReceiveUserId()))// DTO로 변환하여 무한순환 방지
                .collect(Collectors.toSet()) // 중복 제거 (Set 사용)
                .stream().toList();
    }
}




