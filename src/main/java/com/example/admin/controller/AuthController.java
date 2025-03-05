package com.example.admin.controller;

import com.example.admin.entity.TokenResponse;
import com.example.admin.security.JwtTokenProvider;
import com.example.admin.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

/*사용자 인증을 처리*/
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;
    private final UserService userService;

    public AuthController(JwtTokenProvider jwtTokenProvider, AuthenticationManager authenticationManager, UserService userService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.authenticationManager = authenticationManager;
        this.userService = userService;
    }

    // 로그인 엔드포인트
    @PostMapping("/signin")
    public ResponseEntity<TokenResponse> login (@RequestParam String username, @RequestParam String password) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
        );

        String accessToken = jwtTokenProvider.createAccessToken(authentication);
        String refreshToken = jwtTokenProvider.createRefreshToken(username);

        userService.updateRefreshToken(username, refreshToken); // Refresh Token 저장

        return ResponseEntity.ok(new TokenResponse(accessToken, refreshToken));
    }

    // Refresh Token을 이용한 새로운 Access Token 발급
    @PostMapping("/refresh")
    public ResponseEntity<TokenResponse> refresh(@RequestParam String refreshToken) {
        if (!jwtTokenProvider.validateToken(refreshToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        String username = jwtTokenProvider.getUsername(refreshToken);
        String newAccessToken = jwtTokenProvider.createAccessToken(
                new UsernamePasswordAuthenticationToken(username, null, new ArrayList<>())
        );

        return ResponseEntity.ok(new TokenResponse(newAccessToken, refreshToken));
    }
}