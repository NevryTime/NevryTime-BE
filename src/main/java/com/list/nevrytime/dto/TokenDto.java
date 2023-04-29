package com.list.nevrytime.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;


public class TokenDto {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class TokenResponseDto {

        private String grantType;
        private String accessToken;
        private String refreshToken;
        private Long accessTokenExpiresIn;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UseRefreshTokenRequestDto {
        private String refreshToken;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    public static class UseRefreshTokenResponseDto {
        private Boolean success;
        private String accessToken;
    }

    @Getter
    @NoArgsConstructor
    public static class TokenRequestDto {
        private String accessToken;
        private String refreshToken;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class TokenDeleteDto {
        private boolean success;
    }
}

