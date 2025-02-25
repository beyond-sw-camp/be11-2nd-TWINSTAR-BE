package com.TwinStar.TwinStar.comment.service;

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

    @Qualifier("commentLikeRedisTemple")
    private final RedisTemplate<String, Object> commentLikeRedisTemplate;

    public CommentLikeService(CommentLikeRepository commentLikeRepository, CommentRepository commentRepository, UserRepository userRepository, RabbitTemplate rabbitTemplate, @Qualifier("commentLikeRedisTemple")RedisTemplate<String, Object> commentLikeRedisTemplate) {
        this.commentLikeRepository = commentLikeRepository;
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
        this.rabbitTemplate = rabbitTemplate;
        this.commentLikeRedisTemplate = commentLikeRedisTemplate;
    }

    @Transactional
    public CommentLikeResDto commentLikeToggle(Long commentId) {
        String redisKey = "comment:like:" + commentId;

        // 댓글과 유저 정보 조회
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("해당 댓글이 존재하지 않습니다."));
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findById(Long.valueOf(authentication.getName())).orElseThrow(()-> new EntityNotFoundException("user is not found."));

        Optional<CommentLike> commentLikeOpt = commentLikeRepository.findByCommentAndUser(comment, user);
        boolean isLike;

        if (commentLikeOpt.isPresent()) {
            commentLikeRepository.delete(commentLikeOpt.get());
            isLike = false;
            rabbitTemplate.convertAndSend(BACKUP_QUEUE_COMMENT_ML, commentId);
        } else {
            CommentLike newLike = CommentLike.builder()
                    .comment(comment)
                    .user(user)
                    .build();
            commentLikeRepository.save(newLike);
            isLike = true;
            rabbitTemplate.convertAndSend(BACKUP_QUEUE_COMMENT_AL, commentId);
        }

        Long likeCount = commentLikeRepository.countByComment(comment);

        // Redis 업데이트 (데이터 정합성 유지)
        commentLikeRedisTemplate.opsForValue().set(redisKey, String.valueOf(likeCount), 10, TimeUnit.MINUTES);


        return new CommentLikeResDto(likeCount, isLike);
    }
}
