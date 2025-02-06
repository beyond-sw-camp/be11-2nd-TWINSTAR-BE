package com.TwinStar.TwinStar.user.controller;


import com.TwinStar.TwinStar.common.auth.JwtTokenProvider;
import com.TwinStar.TwinStar.user.dto.UserSaveReq;
import com.TwinStar.TwinStar.user.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;
    @Qualifier("rtdb")
    private final RedisTemplate<String,Object> redisTemplate;
    @Value("${jwt.secretKeyRt}")
    private String secretKeyRt;

    public UserController(UserService userService, JwtTokenProvider jwtTokenProvider,@Qualifier("rtdb") RedisTemplate<String, Object> redisTemplate) {
        this.userService = userService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.redisTemplate = redisTemplate;
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@Valid @RequestBody UserSaveReq dto) {
        Long memberId = userService.create(dto);
        return new ResponseEntity<>(memberId, HttpStatus.CREATED);
    }




}
