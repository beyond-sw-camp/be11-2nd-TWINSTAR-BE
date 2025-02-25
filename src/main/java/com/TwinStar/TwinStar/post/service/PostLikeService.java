package com.TwinStar.TwinStar.post.service;

import com.TwinStar.TwinStar.alarm.repository.AlarmRepository;
import com.TwinStar.TwinStar.alarm.service.AlarmService;
import com.TwinStar.TwinStar.post.domain.Post;
import com.TwinStar.TwinStar.post.domain.PostLike;
import com.TwinStar.TwinStar.post.dto.PostLikeResDto;
import com.TwinStar.TwinStar.post.repository.PostLikeRepository;
import com.TwinStar.TwinStar.post.repository.PostRepository;
import com.TwinStar.TwinStar.user.domain.User;
import com.TwinStar.TwinStar.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static com.TwinStar.TwinStar.common.config.RabbitMQConfig.BACKUP_QUEUE_AL;
import static com.TwinStar.TwinStar.common.config.RabbitMQConfig.BACKUP_QUEUE_ML;

@Service
public class PostLikeService {

    private final PostLikeRepository postLikeRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final RabbitTemplate rabbitTemplate;
    private final AlarmService alarmService;
    private final AlarmRepository alarmRepository;

    @Qualifier("postLikeRedisTemple")
    private final RedisTemplate<String, Object> postLikeRedisTemplate;

    public PostLikeService(PostLikeRepository postLikeRepository, PostRepository postRepository, UserRepository userRepository, RabbitTemplate rabbitTemplate, AlarmService alarmService, AlarmRepository alarmRepository, @Qualifier("postLikeRedisTemple")RedisTemplate<String, Object> postLikeRedisTemplate) {
        this.postLikeRepository = postLikeRepository;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.rabbitTemplate = rabbitTemplate;
        this.alarmService = alarmService;
        this.alarmRepository = alarmRepository;
        this.postLikeRedisTemplate = postLikeRedisTemplate;
    }

    @Transactional
    public PostLikeResDto togglePostLike(Long postId) {
        String redisKey = "post:like:" + postId;

        Object cachedValue = postLikeRedisTemplate.opsForValue().get(redisKey);
        Long likeCount = cachedValue != null ? ((Number) cachedValue).longValue() : null;

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findById(Long.valueOf(authentication.getName()))
                .orElseThrow(() -> new EntityNotFoundException("user is not found."));

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시물이 존재하지 않습니다."));

        if (likeCount == null) {
            likeCount = postLikeRepository.countByPost(post);
            postLikeRedisTemplate.opsForValue().set(redisKey, likeCount, 10, TimeUnit.MINUTES);
        }

        Optional<PostLike> postLikeOpt = postLikeRepository.findByPostAndUser(post, user);
        boolean isLike;

        if (postLikeOpt.isPresent()) {
            postLikeRepository.delete(postLikeOpt.get());
            isLike = false;
            rabbitTemplate.convertAndSend(BACKUP_QUEUE_ML, postId);
            likeCount--;
        } else {
            PostLike newLike = PostLike.builder()
                    .post(post)
                    .user(user)
                    .build();
            postLikeRepository.save(newLike);
            isLike = true;
            rabbitTemplate.convertAndSend(BACKUP_QUEUE_AL, postId);
            likeCount++;
        }

        postLikeRedisTemplate.opsForValue().set(redisKey, likeCount, 10, TimeUnit.MINUTES);

        // 게시글 좋아요 알림
        User receiver = post.getUser();
        String content = receiver.getNickName() + "님이 회원님의 게시물을 좋아합니다.";
        String url = "http://localhost:3000/post/detail/" + post.getId();
        if (!alarmRepository.existsByUrlAndContent(url, content)) {
            alarmService.createAlarm(receiver, content, url);
        }

        return new PostLikeResDto(likeCount, isLike);
    }
}