package com.TwinStar.TwinStar.alarm.controller;


import com.TwinStar.TwinStar.alarm.service.AlarmService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/alarm")
public class AlarmController {

    private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();
    private final AlarmService alarmService;

    public AlarmController(AlarmService alarmService) {
        this.alarmService = alarmService;
    }

    @GetMapping("/subscribe")
    public SseEmitter subscribe() {
        SseEmitter emitter = new SseEmitter(30 * 60 * 1000L); // 30분
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getName();
        emitters.put(userId, emitter);

        try {
            // 초기 연결 확인을 위한 더미 이벤트 전송
            emitter.send(SseEmitter.event().name("connect").data("연결 성공"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return emitter;
    }

    @GetMapping("/unsubscribe")
    public void unSubscribe() {
//       연결객체 생성
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getName();
        emitters.remove(userId);
        System.out.println(emitters);
    }

    public void sendNotification(Long userId, String message) {
        SseEmitter emitter = emitters.get(String.valueOf(userId));
        System.out.println(emitter);
        System.out.println("this1");
        if (emitter != null) {
            try {
                System.out.println("this2");
                emitter.send(SseEmitter.event().name("alarm").data(message));
            } catch (IOException e) {
                System.out.println("this 3");
                emitters.remove(userId); // 전송 중 오류 발생하면 제거
            }
        }
    }
}
