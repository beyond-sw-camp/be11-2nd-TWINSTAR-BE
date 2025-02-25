package com.TwinStar.TwinStar.comment.service;

import com.TwinStar.TwinStar.alarm.service.AlarmService;
import com.TwinStar.TwinStar.comment.domain.Comment;
import com.TwinStar.TwinStar.comment.domain.CommentLike;
import com.TwinStar.TwinStar.comment.dto.CommentLikeResDto;
import com.TwinStar.TwinStar.comment.repository.CommentLikeRepository;
import com.TwinStar.TwinStar.comment.repository.CommentRepository;
import com.TwinStar.TwinStar.user.domain.User;
import com.TwinStar.TwinStar.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static com.TwinStar.TwinStar.common.config.RabbitMQConfig.BACKUP_QUEUE_COMMENT_AL;
import static com.TwinStar.TwinStar.common.config.RabbitMQConfig.BACKUP_QUEUE_COMMENT_ML;

@Service
public class CommentLikeService {

    private final CommentLikeRepository commentLikeRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final RabbitTemplate rabbitTemplate;
    private final AlarmService alarmService;

    @Qualifier("commentLikeRedisTemple")
    private final RedisTemplate<String, Object> commentLikeRedisTemplate;

    public CommentLikeService(CommentLikeRepository commentLikeRepository, CommentRepository commentRepository, UserRepository userRepository, RabbitTemplate rabbitTemplate, AlarmService alarmService, @Qualifier("commentLikeRedisTemple")RedisTemplate<String, Object> commentLikeRedisTemplate) {
        this.commentLikeRepository = commentLikeRepository;
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
        this.rabbitTemplate = rabbitTemplate;
        this.alarmService = alarmService;
        this.commentLikeRedisTemplate = commentLikeRedisTemplate;
    }


    @Transactional
    public CommentLikeResDto commentLikeToggle(Long commentId) {
        String redisKey = "comment:like:" + commentId;

        Object cachedValue = commentLikeRedisTemplate.opsForValue().get(redisKey);
        Long likeCount = cachedValue != null ? ((Number) cachedValue).longValue() : null;

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findById(Long.valueOf(authentication.getName()))
                .orElseThrow(() -> new EntityNotFoundException("user is not found."));

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("해당 댓글이 존재하지 않습니다."));

        if (likeCount == null) {
            likeCount = commentLikeRepository.countByComment(comment);
            commentLikeRedisTemplate.opsForValue().set(redisKey, likeCount, 10, TimeUnit.MINUTES);
        }

        Optional<CommentLike> commentLikeOpt = commentLikeRepository.findByCommentAndUser(comment, user);
        boolean isLike;

        if (commentLikeOpt.isPresent()) {
            commentLikeRepository.delete(commentLikeOpt.get());
            isLike = false;
            rabbitTemplate.convertAndSend(BACKUP_QUEUE_COMMENT_ML, commentId);
            likeCount--;
        } else {
            CommentLike newLike = CommentLike.builder()
                    .comment(comment)
                    .user(user)
                    .build();
            commentLikeRepository.save(newLike);
            isLike = true;
            rabbitTemplate.convertAndSend(BACKUP_QUEUE_COMMENT_AL, commentId);
            likeCount++;
        }

        commentLikeRedisTemplate.opsForValue().set(redisKey, likeCount, 10, TimeUnit.MINUTES);

        User receiver = comment.getUser();
        String content = receiver.getNickName() + "님이 회원님의 댓글을 좋아합니다.";
        String url = "http://localhost:3000/post/detail/" + comment.getPost().getId();
        alarmService.createAlarm(receiver, content, url);

        return new CommentLikeResDto(likeCount, isLike);
    }

}
