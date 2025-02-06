package com.TwinStar.TwinStar.common.auth;

import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtil {
    @Value("${jwt.secretKey}")
    private String secretKey;

    // 🔹 JWT에서 Claims(사용자 정보) 추출
    public Claims extractClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // 🔹 토큰에서 사용자 ID 추출
    public Long getUserId(String token) {
        Claims claims = extractClaims(token);
        return Long.parseLong(claims.getSubject());  // subject에 userId 저장됨
    }

    // 🔹 토큰 만료 여부 체크
    public boolean isTokenExpired(String token) {
        return extractClaims(token).getExpiration().before(new Date());
    }

    // 🔹 토큰 검증 (잘못된 토큰이면 예외 발생)
    public boolean validateToken(String token) {
        try {
            extractClaims(token);
            return true;
        } catch (ExpiredJwtException e) {
            throw new JwtException("토큰이 만료되었습니다.");
        } catch (MalformedJwtException e) {
            throw new JwtException("토큰 형식이 올바르지 않습니다.");
        } catch (SignatureException e) {
            throw new JwtException("토큰 서명이 유효하지 않습니다.");
        } catch (Exception e) {
            throw new JwtException("토큰이 유효하지 않습니다.");
        }
    }
}
