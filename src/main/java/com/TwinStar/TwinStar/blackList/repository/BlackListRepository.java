package com.TwinStar.TwinStar.blackList.repository;

import com.TwinStar.TwinStar.blackList.domain.BlackList;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface BlackListRepository extends JpaRepository<BlackList,Long> {
    Optional<BlackList> findByUserIdAndBlockedUserId(Long userId, Long blockUserId);
    Page<BlackList> findByUserId(Long userId, Pageable pageable);

    // 차단 해제 시간이 지난 목록 조회
    List<BlackList> findByBanCloseTimeBefore(LocalDateTime now);
}
