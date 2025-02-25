package com.TwinStar.TwinStar.post.service;

import com.TwinStar.TwinStar.common.config.RabbitMQConfig;
import com.TwinStar.TwinStar.user.domain.User;
import com.TwinStar.TwinStar.user.dto.ChatUserListDto;
import com.TwinStar.TwinStar.user.repository.UserRepository;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class LikeService {
    private final RedisTemplate<String,Object> postLikeRedisTemplate;
    private final UserRepository userRepository;
    private final RabbitTemplate rabbitTemplate;

    public LikeService(@Qualifier("postLikeRedisTemple") RedisTemplate<String, Object> postLikeRedisTemplate, UserRepository userRepository, RabbitTemplate rabbitTemplate) {
        this.postLikeRedisTemplate = postLikeRedisTemplate;
        this.userRepository = userRepository;
        this.rabbitTemplate = rabbitTemplate;
    }

    public Map<String, Object> toggleLike(Long postId){
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        String key = "post:like:" + postId;

        boolean hasLiked = postLikeRedisTemplate.opsForSet().isMember(key, userId);


        if (hasLiked){
            postLikeRedisTemplate.opsForSet().remove(key,userId);
//            RabbitMQ에 메시지 전송 (좋아요 감소)
            rabbitTemplate.convertAndSend(RabbitMQConfig.BACKUP_QUEUE_ML,createLikeMessage(postId,userId,"decrease"));
        } else {
            postLikeRedisTemplate.opsForSet().add(key,userId);
//            RabbitMQ에 메시지 전송 (좋아요 증가)
            rabbitTemplate.convertAndSend(RabbitMQConfig.BACKUP_QUEUE_AL,createLikeMessage(postId,userId,"increase"));
        }

        return getLikeStatus(postId,Long.valueOf(userId));
    }

    public Map<String,Object> getLikeStatus(Long postId, Long userId){
        String key = "post:like:" + postId;

//        좋아요한 유저ID 목록 가져오기
        Set<Object> likeUserIds = postLikeRedisTemplate.opsForSet().members(key);
        List<Long> userIds = likeUserIds.stream()
                .map(id -> Long.parseLong(id.toString())).collect(Collectors.toList()); //set을 list로 형변환

//        좋아요한 유저 목록 가져오기
        List<User> users = userRepository.findAllById(userIds);

//       유저목록에서 좋아요 유저 목록에 맞는 dto로 변환
        List<ChatUserListDto> likeUsers = new ArrayList<>();
        for (User user : users){
            likeUsers.add(new ChatUserListDto(user));
        }

//        좋아요 개수
        Long likeCount = postLikeRedisTemplate.opsForSet().size(key);

//        반환데이터
        Map<String, Object> likeStatus = new HashMap<>();
        likeStatus.put("likedUsers",likeUsers);
        likeStatus.put("likeCount",likeCount);

        return likeStatus;
    }

    /**
     * 좋아요 이벤트 메시지를 생성하는 메서드
     */
    private Map<String, String> createLikeMessage(Long postId, String userId, String action) {
        Map<String, String> message = new HashMap<>();
        message.put("postId", postId.toString());
        message.put("userId", userId);
        message.put("action", action); // "increase" 또는 "decrease"
        return message;
    }
}
