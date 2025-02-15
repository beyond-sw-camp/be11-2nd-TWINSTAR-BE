package com.TwinStar.TwinStar.common.auth;

import com.TwinStar.TwinStar.user.domain.User;
import com.TwinStar.TwinStar.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;



public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final UserRepository userRepository;

    public CustomAuthenticationFilter(AuthenticationManager authenticationManager, UserRepository userRepository) {
        super(); //부모 생성자에서 직접 AuthenticationManager를 받지 않음
        this.setAuthenticationManager(authenticationManager);
        this.userRepository = userRepository;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        String userId = obtainUsername(request);//로그인 아이디값
        String password = obtainPassword(request);//로그인 비밀번호 값

        // 사용자 정보 조회
        User user = userRepository.findByEmail(userId)
                .orElseThrow(() -> new BadCredentialsException("사용자를 찾을 수 없습니다."));

        // 정지된 사용자인지 확인
        if (user.isBanned()) {
            throw new BadCredentialsException("계정이 정지되었습니다. 정지 해제 날짜: " +
                    (user.getBanCloseTime() != null ? user.getBanCloseTime() : "무기한 정지"));
        }

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(userId, password);

        return getAuthenticationManager().authenticate(authenticationToken);
    }
}
