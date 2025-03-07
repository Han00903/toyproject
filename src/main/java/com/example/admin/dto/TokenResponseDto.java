package com.example.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

//  로그인 & Refresh API 응답을 위한 DTO
@Getter
@AllArgsConstructor
public class TokenResponseDto {

    private String accessToken;
    private String refreshToken;

    public String getAccessToken() {

        return accessToken;

    }

    public String getRefreshToken() {

        return refreshToken;
    }
}