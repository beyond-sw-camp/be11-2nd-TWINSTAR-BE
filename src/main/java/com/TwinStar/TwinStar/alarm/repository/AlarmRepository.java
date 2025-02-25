package com.TwinStar.TwinStar.alarm.repository;

import com.TwinStar.TwinStar.alarm.domain.Alarm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AlarmRepository extends JpaRepository<Alarm,Long> {
}
