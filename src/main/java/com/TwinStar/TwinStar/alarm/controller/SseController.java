package com.TwinStar.TwinStar.alarm.controller;

import com.TwinStar.TwinStar.alarm.domain.Alarm;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SseController {

    private final Map<String, SseEmitter> emitterMap = new ConcurrentHashMap<>();

    //    사용자의 서버 연결 요청을 통해 연결정보에 등록.
    @GetMapping("/subscribe")
    public SseEmitter subscribe(){
//        연결 객체 생성
        SseEmitter sseEmitter = new SseEmitter(14400*60*1000L); // 10일 유효시간

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String id = authentication.getName();
        emitterMap.put(id, sseEmitter);

        try{
            sseEmitter.send(SseEmitter.event().name("connect").data("연결완료"));
        }catch (IOException e){
            e.printStackTrace();
        }

        return sseEmitter;
    }

    @GetMapping("/unsubscribe")
    public void unSubscribe() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String id = authentication.getName();
        emitterMap.remove(id);
        System.out.println(emitterMap);
    }

    //    특정 사용자에게 message 발송
    public void publishMessage(String id, Alarm alarm){
        SseEmitter sseEmitter = emitterMap.get(id);
        try{
            sseEmitter.send(SseEmitter.event().name("alarm").data(alarm));
            System.out.println(emitterMap);
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
