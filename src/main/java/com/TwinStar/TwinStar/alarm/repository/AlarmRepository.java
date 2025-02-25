package com.TwinStar.TwinStar.alarm.repository;

import com.TwinStar.TwinStar.alarm.domain.Alarm;
import com.TwinStar.TwinStar.user.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AlarmRepository extends JpaRepository<Alarm,Long> {
    Page<Alarm> findByUserOrderByCreatedTimeDesc(User user, Pageable pageable);
}
