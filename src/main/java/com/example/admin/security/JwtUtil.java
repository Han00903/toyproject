package com.example.admin.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.stereotype.Component;

import java.util.Date;

/*토큰 생성 및 검증 관련 유틸리티 메서드 담당 */
@Component
public class JwtUtil {

    private final String secretKey = "mySecretKey";  // 환경 변수로 설정하는 것이 안전함
    private final long accessTokenValidity = 3600000; // 1시간 (1시간)
    private final long refreshTokenValidity = 1000 * 60 * 60 * 24 * 7; // 7일 (7일)

    // Access Token 생성
    public String createAccessToken(String username) {
        return generateToken(username, accessTokenValidity);
    }

    // Refresh Token 생성
    public String createRefreshToken(String username) {
        return generateToken(username, refreshTokenValidity);
    }

    // 공통 토큰 생성 메서드
    private String generateToken(String username, long validity) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + validity);

        return JWT.create()
                .withSubject(username)
                .withIssuedAt(now)
                .withExpiresAt(expiryDate)
                .sign(Algorithm.HMAC256(secretKey));
    }

    // 토큰에서 유저 이름 추출
    public String getUsername(String token) {
        DecodedJWT decodedJWT = JWT.require(Algorithm.HMAC256(secretKey))
                .build()
                .verify(token);
        return decodedJWT.getSubject();
    }

    // 토큰 유효성 검증
    public boolean validateToken(String token) {
        try {
            DecodedJWT decodedJWT = JWT.require(Algorithm.HMAC256(secretKey))
                    .build()
                    .verify(token);
            return !decodedJWT.getExpiresAt().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }
}