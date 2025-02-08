package com.TwinStar.TwinStar.user.controller;


import com.TwinStar.TwinStar.common.auth.JwtTokenProvider;
import com.TwinStar.TwinStar.common.dto.CommonDto;
import com.TwinStar.TwinStar.user.domain.User;
import com.TwinStar.TwinStar.user.dto.*;
import com.TwinStar.TwinStar.user.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
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
    // ✅ 리프레시 토큰을 이용한 액세스 토큰 재발급
    // --to do
//    API 요청을 보낼 때, 액세스 토큰이 만료되었는지 확인
//    만료되었다면 /user/refresh-token API를 호출하여 새 액세스 토큰을 받아오기
//    새로운 액세스 토큰으로 다시 API 요청을 보냄
//    새로 받은 액세스 토큰을 저장 (로컬 스토리지 or 쿠키)
    @PostMapping("/refresh-token")
    public ResponseEntity<Map<String, String>> refreshAccessToken(@RequestHeader("Authorization") String refreshToken) {
        if (refreshToken.startsWith("Bearer ")) {
            refreshToken = refreshToken.substring(7);
        }

        String newAccessToken = jwtTokenProvider.refreshAccessToken(refreshToken);

        Map<String, String> response = new HashMap<>();
        response.put("accessToken", newAccessToken);
        return ResponseEntity.ok(response);
    }

//    비밀번호 변경
    @PatchMapping("/{id}/password")
    public ResponseEntity<?> changePassword(@PathVariable Long id, @RequestBody PasswordChangeRequest request
            , Authentication authentication){

        userService.changePassword(id, request,authentication);
        return new ResponseEntity<>(new CommonDto(HttpStatus.OK.value(),"Password changed successfully",null),HttpStatus.OK);

    }



////    상대방 프로필 들어가면 정보를 얻는다.
//    @GetMapping("/detail/{id}")
//    public ResponseEntity<?> userDetail(@PathVariable Long id){
//        UserProfileDto dto = userService.searchProfile(id);
//        return new ResponseEntity<>(new CommonDto(HttpStatus.OK.value(), "memberDetailLest is found",dto),HttpStatus.OK);
//
//    }
//
////    내 프로필 정보 조회
//    @GetMapping("/myProfile")
//    public ResponseEntity<?> myProfile(){
//        UserProfileDto dto = userService.searchProfile();
//        return new ResponseEntity<>(new CommonDto(HttpStatus.OK.value(), "memberDetailLest is found",dto),HttpStatus.OK);
//    }
//
//    //    사용자 프로필 수정
//    @PatchMapping("/{id}/profile")
//    public ResponseEntity<?> updateProfile(@PathVariable Long userId,
//                                                @RequestBody UserProfileUpdateDto updateDto) {
//        userService.updateUserProfile(userId, updateDto);
//        return new ResponseEntity<>(new CommonDto(HttpStatus.OK.value(), "Profile updated successfully.","null"),HttpStatus.OK);
//    }

// 관리자용 유저목록 조회
    @GetMapping("/admin/user/list")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> list(){
        List<UserListDto> userListDto = userService.userList();
        return new ResponseEntity<>(userListDto,HttpStatus.OK);
    }

//    // JWT 기반 회원 탈퇴 API
//    @DeleteMapping("/del")
//    public ResponseEntity<?> deleteUser() {
//        userService.deleteUser();
//        return new ResponseEntity<>(new CommonDto(HttpStatus.OK.value(), "Profile updated successfully.","null"),HttpStatus.OK);
//    }

}
