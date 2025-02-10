package com.TwinStar.TwinStar.user.service;

import com.TwinStar.TwinStar.user.domain.User;
import com.TwinStar.TwinStar.user.domain.UserStatus;
import com.TwinStar.TwinStar.user.repository.UserRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserBanScheduler {
    private final UserRepository userRepository;

    public UserBanScheduler(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

//    스케쥴러 기준은 1시간마다로 기준이 하루일 경우, 밤에 정지당하면 정지가 하루 없는 격임
    @Scheduled(cron = "0 0 * * * *")
    public void unbanExpiredUsers() {
//        BAN인 유저 조회
        List<User> expiredBans = userRepository.findBannedUsers(UserStatus.BAN)
                .stream()
//                정지해제 시간이 넘어간 유저 필터링
                .filter(user -> user.getBanCloseTime() != null && user.getBanCloseTime().isBefore(LocalDateTime.now()))
                .toList(); // Java 16 이상

        for (User user : expiredBans) {
            user.unban();
            userRepository.save(user);
        }

        if (!expiredBans.isEmpty()) {
            System.out.println("정지 해제된 사용자: " + expiredBans.size() + "명");
        }
    }
}
