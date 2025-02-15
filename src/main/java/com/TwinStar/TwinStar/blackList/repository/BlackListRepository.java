package com.TwinStar.TwinStar.blackList.repository;

import com.TwinStar.TwinStar.blackList.domain.BlackList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BlackListRepository extends JpaRepository<BlackList,Long> {
    Optional<BlackList> findByUserIdAndBlockedUserId(Long userId, Long blockUserId);
    List<BlackList> findByUserId(Long userId);

}
