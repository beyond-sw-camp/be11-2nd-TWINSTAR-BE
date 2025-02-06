package com.TwinStar.TwinStar.user.controller;


import com.TwinStar.TwinStar.common.auth.JwtTokenProvider;
import com.TwinStar.TwinStar.common.dto.CommonDto;
import com.TwinStar.TwinStar.user.domain.User;
import com.TwinStar.TwinStar.user.dto.LoginDto;
import com.TwinStar.TwinStar.user.dto.UserListDto;
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
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;
    @Qualifier("rtdb")
    private final RedisTemplate<String, Object> redisTemplate;
    @Value("${jwt.secretKeyRt}")
    private String secretKeyRt;


    public UserController(UserService userService, JwtTokenProvider jwtTokenProvider, @Qualifier("rtdb") RedisTemplate<String, Object> redisTemplate) {
        this.userService = userService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.redisTemplate = redisTemplate;
    }

    @PostMapping("/doLogin")
    public ResponseEntity<?> doLogin(@RequestBody LoginDto dto) {
//        id,email, password 검증
        User user = userService.login(dto);
//        토큰 생성 및 return
        String token = jwtTokenProvider.createToken(user.getId(), user.getEmail(), user.getAdminYn().toString());
        String refreshToken = jwtTokenProvider.createRefreshToken(user.getId(), user.getEmail(), user.getAdminYn().toString());
//        redis에 rt저장
        redisTemplate.opsForValue().set(user.getEmail(), refreshToken, 200, TimeUnit.DAYS);//200일 ttl
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

//    로그아웃(토큰 무효화)
        @PostMapping("/logout")
        public ResponseEntity<?> logout () {
            return null;
        }

// 엑세스 토큰 재발행
        @PostMapping("/refresh-token")
        public ResponseEntity<?> refreshToken () {
            return null;
        }
//    회원탈퇴
        @DeleteMapping("/delete/{userid}")
        public ResponseEntity<?> del_user () {

            return null;
        }

//    사용자 상세조회
        @GetMapping("/detail/{userid}")
        public ResponseEntity<?> userDetail () {
            List<UserListDto> userListDtos = userService.findAll();
            return new ResponseEntity<>(new CommonDto(HttpStatus.OK.value(), "user list is found", userListDtos), HttpStatus.OK);
        }
//    현재 로그인한 사용자 정보 조회
        @GetMapping("/myInfo")
        public ResponseEntity<?> myInfo () {
            return null;
        }


//  사용자 프로필 정보 수정
        @PatchMapping("/profile/update/{userid}")
        public ResponseEntity<?> profileUpdate () {
            return null;
        }
//   비밀번호 변경
        @PatchMapping("/profile/update/{userid}")
        public ResponseEntity<?> passwordChange () {
            return null;
        }

//    프로필 이미지 변경
        @PatchMapping("/profile/update/{userid}")
        public ResponseEntity<?> profileImgUpdate () {
            return null;
        }

//    사용자 공개 범위 변경
        @PatchMapping("/status/{userid}")
        public ResponseEntity<?> userStatusUpdate () {
            return null;
        }


//    관리자 권한 부여/회수
        @PatchMapping("/admin/{userid}")
        public ResponseEntity<?> adminUpdate () {
            return null;
        }

//    특정 사용자 신고
        @PostMapping("/report/{userid}")
        public ResponseEntity<?> userReport () {
            return null;
        }

//    사용자 검색
        @GetMapping("/search")
        public ResponseEntity<?> userSearch () {
            return null;
        }

//    관리자용 엔드포인트

//    전체 사용자 리스트 조회
        @GetMapping("/admin/userList")
        public ResponseEntity<?> adminUserList () {
            return null;
        }

//    신고 목록 조회
        @GetMapping("/reports")
        public ResponseEntity<?> adminReportList () {
            return null;
        }
//   신고처리

//특정 사용자 정지
        @GetMapping("/admin/report/{userid}")
        public ResponseEntity<?> reported () {
            return null;
            String newAccessToken = jwtTokenProvider.refreshAccessToken(refreshToken);

            Map<String, String> response = new HashMap<>();
            response.put("accessToken", newAccessToken);
            return ResponseEntity.ok(response);

        }
    }
}
