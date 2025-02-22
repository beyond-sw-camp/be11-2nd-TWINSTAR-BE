package com.TwinStar.TwinStar.common.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Date;

@Component
public class JwtTokenProvider {
    @Value("${jwt.expiration}")
    private int expiration;
    @Value("${jwt.secretKey}")
    private String secretKey;

    @Value("${jwt.secretKeyRt}")
    private String secretKeyRt;

    @Value("${jwt.expirationRt}")
    private int expirationRt;

    private Key ENCRYPT_SECRET_KEY;
    private Key ENCRYPT_RT_SECRET_KEY;

    @PostConstruct
    public void init() {
        ENCRYPT_SECRET_KEY = new SecretKeySpec(java.util.Base64.getDecoder().decode(secretKey), SignatureAlgorithm.HS512.getJcaName());
        ENCRYPT_RT_SECRET_KEY = new SecretKeySpec(java.util.Base64.getDecoder().decode(secretKeyRt), SignatureAlgorithm.HS512.getJcaName());
    }

    public String createToken(Long id, String email, String nickName, String role) {
        Claims claims = Jwts.claims().setSubject(String.valueOf(id));
        claims.put("role", role);
        claims.put("email", email);
        claims.put("nickName", nickName);
        Date now = new Date();

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + expiration * 60 * 1000L)) // 액세스 토큰 유효 기간 (30분)
                .signWith(ENCRYPT_SECRET_KEY)
                .compact();
    }

    public String createRefreshToken(Long id, String email, String role) {
        Claims claims = Jwts.claims().setSubject(String.valueOf(id));
        claims.put("email", email);
        claims.put("role", role);
        Date now = new Date();

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + expirationRt * 60 * 1000L)) // 리프레시 토큰 유효 기간 (7일)
                .signWith(ENCRYPT_RT_SECRET_KEY)
                .compact();
    }

    // ✅ 토큰 검증
    public boolean validateToken(String token, boolean isRefreshToken) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(isRefreshToken ? ENCRYPT_RT_SECRET_KEY : ENCRYPT_SECRET_KEY)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    // ✅ 토큰에서 클레임(정보) 추출
    public Claims getClaims(String token, boolean isRefreshToken) {
        return Jwts.parserBuilder()
                .setSigningKey(isRefreshToken ? ENCRYPT_RT_SECRET_KEY : ENCRYPT_SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // ✅ 리프레시 토큰을 이용한 액세스 토큰 재발행
    public String refreshAccessToken(String refreshToken) {
        if (!validateToken(refreshToken, true)) {
            throw new RuntimeException("유효하지 않은 리프레시 토큰입니다.");
        }

        Claims claims = getClaims(refreshToken, true);
        Long userId = Long.parseLong(claims.getSubject());
        String email = claims.get("email", String.class);
        String role = claims.get("role", String.class);
        String nickName = claims.get("nickName", String.class);

        return createToken(userId, email, nickName, role);
    }
}