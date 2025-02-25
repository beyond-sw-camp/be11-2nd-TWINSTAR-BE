package com.TwinStar.TwinStar.comment.service;

import com.TwinStar.TwinStar.comment.dto.CommentUserListDto;
import com.TwinStar.TwinStar.common.config.RabbitMQConfig;
import com.TwinStar.TwinStar.user.domain.User;
import com.TwinStar.TwinStar.user.dto.ChatUserListDto;
import com.TwinStar.TwinStar.user.repository.UserRepository;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class CommentLikeService {
    private final RedisTemplate<String, Object> commentLikeRedisTemple;
    private final UserRepository userRepository;
    private final RabbitTemplate rabbitTemplate;

    public CommentLikeService(RedisTemplate<String, Object> commentLikeRedisTemple, UserRepository userRepository, RabbitTemplate rabbitTemplate) {
        this.commentLikeRedisTemple = commentLikeRedisTemple;
        this.userRepository = userRepository;
        this.rabbitTemplate = rabbitTemplate;
    }

    public Map<String,Object> commentLikeToggle(Long commentId){
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        String key = "comment:like:" + userId;

        boolean hasLiked = commentLikeRedisTemple.opsForSet().isMember(key, userId);

        if (hasLiked){
            commentLikeRedisTemple.opsForSet().remove(key,userId);
            rabbitTemplate.convertAndSend(RabbitMQConfig.BACKUP_QUEUE_COMMENT_ML,createLikeMessage(commentId,userId,"decrease"));
        }
        else {
            commentLikeRedisTemple.opsForSet().add(key,userId);
            rabbitTemplate.convertAndSend(RabbitMQConfig.BACKUP_QUEUE_COMMENT_AL,createLikeMessage(commentId,userId,"increase"));
        }

        return getCommentLikeStatus(commentId,Long.valueOf(userId));
    }

    public Map<String,Object> getCommentLikeStatus(Long commentId, Long userId){
        String key = "comment:like:" + commentId;

//        좋아요한 유저ID 목록 가져오기
        Set<Object> likeUserIds = commentLikeRedisTemple.opsForSet().members(key);
        List<Long> userIds = likeUserIds.stream()
                .map(id -> Long.parseLong(id.toString())).collect(Collectors.toList()); //set을 list로 형변환

//        좋아요한 유저 목록 가져오기
        List<User> users = userRepository.findAllById(userIds);

//       유저목록에서 좋아요 유저 목록에 맞는 dto로 변환
        List<CommentUserListDto> commentLikeUsers = new ArrayList<>();
        for (User user : users){
            commentLikeUsers.add(new CommentUserListDto(user));
        }

//        좋아요 개수
        Long commentLikeCount = commentLikeRedisTemple.opsForSet().size(key);

//        반환데이터
        Map<String, Object> likeStatus = new HashMap<>();
        likeStatus.put("likedUsers",commentLikeUsers);
        likeStatus.put("likeCount",commentLikeCount);

        return likeStatus;
    }

    /**
     * 좋아요 이벤트 메시지를 생성하는 메서드
     */
    private Map<String, String> createLikeMessage(Long commentId, String userId, String action) {
        Map<String, String> message = new HashMap<>();
        message.put("postId", commentId.toString());
        message.put("userId", userId);
        message.put("action", action); // "increase" 또는 "decrease"
        return message;
    }
}
