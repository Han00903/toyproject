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

/* Spring Security와 연결되는 인증 관련 기능 담당 */
@Component
public class JwtTokenProvider {

    private final String secretKey = "yourSecretKey"; // 비밀키 (보안상 환경변수로 관리하기)

    // HTTP 요청에서 토큰 추출
    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7); // "Bearer " 제거하고 토큰 반환
        }
        return null;
    }

    // 토큰에서 Authentication 객체 생성
    public Authentication getAuthentication(String token) {
        String username = getUsername(token);

        // 역할은 실제 요구 사항에 맞게 설정
        ArrayList<Object> roles = new ArrayList<>();
        roles.add("ROLE_USER");

        return new UsernamePasswordAuthenticationToken(username, null, AuthorityUtils.createAuthorityList(roles.toArray(new String[0])));
    }

    // Access Token 생성
    public String createAccessToken(Authentication authentication) {
        return JWT.create()
                .withSubject(authentication.getName())
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) // 1시간
                .sign(Algorithm.HMAC256(secretKey));
    }

    // Refresh Token 생성
    public String createRefreshToken(String username) {
        return JWT.create()
                .withSubject(username)
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 7)) // 7일
                .sign(Algorithm.HMAC256(secretKey));
    }

    // Token 검증
    public boolean validateToken(String token) {
        try {
            DecodedJWT decodedJWT = JWT.require(Algorithm.HMAC256(secretKey))
                    .build()
                    .verify(token);
            return !decodedJWT.getExpiresAt().before(new Date()); // 만료되지 않았는지 확인
        } catch (Exception e) {
            return false;
        }
    }

    // Token에서 Username 추출
    public String getUsername(String token) {
        DecodedJWT decodedJWT = JWT.require(Algorithm.HMAC256(secretKey))
                .build()
                .verify(token);
        return decodedJWT.getSubject();
    }
}
