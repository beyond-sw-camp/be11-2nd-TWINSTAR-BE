package com.TwinStar.TwinStar.alarm.service;

import com.TwinStar.TwinStar.alarm.domain.Alarm;
import com.TwinStar.TwinStar.alarm.dto.AlarmCommonResDto;
import com.TwinStar.TwinStar.alarm.dto.AlarmResDto;
import com.TwinStar.TwinStar.alarm.repository.AlarmRepository;
import com.TwinStar.TwinStar.user.domain.User;
import com.TwinStar.TwinStar.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Transactional
public class AlarmService {
    private final AlarmRepository alarmRepository;
    private final UserRepository userRepository;
    private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();

    public AlarmService(AlarmRepository alarmRepository, UserRepository userRepository) {
        this.alarmRepository = alarmRepository;
        this.userRepository = userRepository;
    }

    public void createAlarm(User receiver, String content, String url){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User sender = userRepository.findById(Long.valueOf(authentication.getName())).orElseThrow(()-> new EntityNotFoundException("user is not found."));
        if (sender.equals(receiver)){return ;}
        Alarm alarm = Alarm.builder()
                .user(receiver)
                .sender(sender)
                .url(url)
                .content(content)
                .build();
        alarmRepository.save(alarm);

        AlarmCommonResDto alarmCommonResDto = AlarmCommonResDto.builder()
                .url(url)
                .content(content)
                .build();
        sendNotification(receiver.getId(), alarmCommonResDto);
    }

    public void sendNotification(Long userId, AlarmCommonResDto dto) {
        SseEmitter emitter = emitters.get(String.valueOf(userId));
        if (emitter != null) {
            try {
                emitter.send(SseEmitter.event().name("alarm").data(dto));
            } catch (IOException e) {
                emitters.remove(String.valueOf(userId)); // 전송 중 오류 발생하면 제거
            }
        }
    }

    public SseEmitter subscribe(Long userId) {
        SseEmitter emitter = new SseEmitter(60 * 1000L); // 30분
        emitters.put(String.valueOf(userId), emitter);

        try {
            emitter.send(SseEmitter.event().name("connect").data("연결 성공"));
        } catch (IOException e) {
            emitters.remove(String.valueOf(userId));
        }
        return emitter;
    }

    public void unsubscribe(Long userId) {
        emitters.remove(String.valueOf(userId));
    }

//    알림 리스트 불러오기
    public Page<AlarmResDto> getAlarms(PageRequest pageRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findById(Long.valueOf(authentication.getName())).orElseThrow(()-> new EntityNotFoundException("user is not found."));

        Page<Alarm> alarmPage = alarmRepository.findByUserOrderByCreatedTimeDesc(user, pageRequest);

        return alarmPage.map(alarm -> AlarmResDto.builder()
                .id(alarm.getId())
                .senderId(alarm.getSender().getId())
                .profileImage(alarm.getSender().getProfileImg())
                .content(alarm.getContent())
                .url(alarm.getUrl())
                .createdTime(alarm.getCreatedTime())
                .build());
    }

}
