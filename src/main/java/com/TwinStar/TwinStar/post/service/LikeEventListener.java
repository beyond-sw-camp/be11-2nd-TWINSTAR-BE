package com.TwinStar.TwinStar.post.service;

import com.TwinStar.TwinStar.common.config.RabbitMQConfig;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;


import java.nio.charset.StandardCharsets;
import java.util.Map;


@Component
public class LikeEventListener {
    private final RedisTemplate<String, String> postLikeRedisTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();//json변환기

    public LikeEventListener(RedisTemplate<String, String> postLikeRedisTemplate) {
        this.postLikeRedisTemplate = postLikeRedisTemplate;
    }

    @RabbitListener(queues = RabbitMQConfig.BACKUP_QUEUE_AL)
    public void processLikeAddition(Message message) {
        try {
            String json = new String(message.getBody(), StandardCharsets.UTF_8);

            // JSON -> Map 변환
            Map<String, String> data = objectMapper.readValue(json, new TypeReference<>() {
            });

            String postId = data.get("postId");
            String userId = data.get("userId");

            String key = "post:like:" + postId;
            postLikeRedisTemplate.opsForSet().add(key, userId);
            System.out.println(" [백업] 좋아요 추가: " + userId + " -> post " + postId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RabbitListener(queues = RabbitMQConfig.BACKUP_QUEUE_ML)
    public void processLikeRemoval(Message message) {
        try {
            String json = new String(message.getBody(), StandardCharsets.UTF_8);
            Map<String, String> data = objectMapper.readValue(json, new TypeReference<>() {
            });

            String postId = data.get("postId");
            String userId = data.get("userId");

            String key = "post:like:" + postId;
            postLikeRedisTemplate.opsForSet().remove(key, userId);
            System.out.println(" [백업] 좋아요 제거: " + userId + " -> post " + postId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
