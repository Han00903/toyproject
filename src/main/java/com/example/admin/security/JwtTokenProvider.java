package com.example.admin.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;

/*JWT 토큰 생성 검증 */
@Component
public class JwtTokenProvider {

    private final String secretKey = "mySecretKey";  // 이 값을 환경 변수로 처리하기
    private final long accessTokenValidity = 3600000; // 1시간 (1시간)
    private final long refreshTokenValidity = 1000 * 60 * 60 * 24 * 7; // 7일 (7일)

    // Access Token 생성
    public String createAccessToken(Authentication authentication) {
        String username = authentication.getName();
        return generateToken(username, accessTokenValidity);
    }

    // Refresh Token 생성
    public String createRefreshToken(String username) {
        return generateToken(username, refreshTokenValidity);
    }

    // 토큰 생성 공통 로직
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

    // HTTP 요청에서 토큰 추출
    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7); // "Bearer " 제거하고 토큰만 반환
        }
        return null;
    }

    // 토큰에서 Authentication 객체 생성
    public Authentication getAuthentication(String token) {
        String username = getUsername(token);

        // 예를 들어, 사용자의 권한을 "USER"로 고정하여 Authentication을 생성
        // 실제로는 권한 정보를 담은 Claim을 토큰에 포함시켜서 동적으로 권한을 부여할 수 있습니다.
        ArrayList<Object> roles = new ArrayList<>();
        roles.add("ROLE_USER");  // 역할은 실제 요구 사항에 맞게 설정

        return new UsernamePasswordAuthenticationToken(username, null, AuthorityUtils.createAuthorityList(roles.toArray(new String[0])));
    }
}