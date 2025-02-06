package com.TwinStar.TwinStar.user.controller;


import com.TwinStar.TwinStar.common.auth.JwtTokenProvider;
import com.TwinStar.TwinStar.user.domain.User;
import com.TwinStar.TwinStar.user.dto.LoginDto;
import com.TwinStar.TwinStar.user.dto.UserSaveReq;
import com.TwinStar.TwinStar.user.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

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

    @PostMapping("/doLogin")
    public ResponseEntity<?> doLogin(@RequestBody LoginDto dto) {
//        id,email, password 검증
        User user = userService.login(dto);
//        토큰 생성 및 return
        String token = jwtTokenProvider.createToken(user.getId(),user.getEmail(),user.getAdminYn().toString());
        String refreshToken = jwtTokenProvider.createRefreshToken(user.getId(),user.getEmail(),user.getAdminYn().toString());
//        redis에 rt저장
        redisTemplate.opsForValue().set(user.getEmail(),refreshToken,200, TimeUnit.DAYS);//200일 ttl
//        사용자에게 at,rt지급

        Map<String, Object> loginInfo = new HashMap<>();
        loginInfo.put("id", user.getId());
        loginInfo.put("token", token);
        loginInfo.put("refreshToken", refreshToken);
        return new ResponseEntity<>(loginInfo, HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@Valid @RequestBody UserSaveReq dto) {
        Long memberId = userService.create(dto);
        return new ResponseEntity<>(memberId, HttpStatus.CREATED);
    }





}
