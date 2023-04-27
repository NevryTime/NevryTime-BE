package com.list.nevrytime.dto;

import com.list.nevrytime.entity.Heart;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class HeartDto {

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class HeartRequestDto {
        private Long contentId;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class HeartResponseDto {

        private boolean success;
        private Long memberId;
        private Long contentId;

        public static HeartResponseDto of(Heart heart) {
            return new HeartResponseDto(true, heart.getMember().getId(),heart.getContent().getId());
        }
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class HeartDeleteResponseDto {
        private boolean success;
    }
}
