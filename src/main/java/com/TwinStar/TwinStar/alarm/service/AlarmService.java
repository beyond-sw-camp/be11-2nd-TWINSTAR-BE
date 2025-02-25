package com.TwinStar.TwinStar.alarm.service;

import com.TwinStar.TwinStar.alarm.repository.AlarmRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class AlarmService {
    private final AlarmRepository alarmRepository;

    public AlarmService(AlarmRepository alarmRepository) {
        this.alarmRepository = alarmRepository;
    }


}
