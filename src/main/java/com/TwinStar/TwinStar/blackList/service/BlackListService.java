package com.TwinStar.TwinStar.blackList.service;

import com.TwinStar.TwinStar.blackList.domain.BlackList;
import com.TwinStar.TwinStar.blackList.repository.BlackListRepository;
import com.TwinStar.TwinStar.common.exception.CustomException;
import com.TwinStar.TwinStar.user.domain.User;
import com.TwinStar.TwinStar.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class BlackListService {
    @Autowired
    private BlackListRepository blacklistRepository;

    @Autowired
    private UserRepository userRepository;

//    차단
    public BlackList blockUser(Long userId, Long blockedUserId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자가 없다"));
        User blockedUser = userRepository.findById(blockedUserId)
                .orElseThrow(() -> new RuntimeException("차단할 사용자를 찾을 수 없다"));

        // 이미 차단된 경우 예외 발생
        boolean isAlreadyBlocked = blacklistRepository.findByUserIdAndBlockedUserId(userId, blockedUserId).isPresent();
        if (isAlreadyBlocked) {
            throw new CustomException("이미 차단된 사용자입니다.");
        }

        BlackList blacklist = BlackList.builder()
                .user(user)
                .blockedUser(blockedUser)
                .build();

        return blacklistRepository.save(blacklist);
    }

//    차단해제
    public void unblockUser(Long userId, Long blockedUserId) {
        Optional<BlackList> blacklist = blacklistRepository.findByUserIdAndBlockedUserId(userId, blockedUserId);
        if (blacklist.isEmpty()) {
            throw new CustomException("차단기록을 찾을 수 없습니다.");
        }

        if (blacklist.isPresent()){//옵셔널 안에 값이 존재하는지
            BlackList foundBlackList = blacklist.get();//옵셔널에서 객체를 가져옴
            blacklistRepository.delete(foundBlackList);//삭제
        }
    }

//    차단목록 조회
    public List<BlackList> getBlockedUsers(Long userId) {
        return blacklistRepository.findByUserId(userId);
    }
}
