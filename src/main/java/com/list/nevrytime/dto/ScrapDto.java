package com.list.nevrytime.dto;

import com.list.nevrytime.entity.Scrap;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class ScrapDto {

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ScrapRequestDto {
        private Long contentId;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ScrapResponseDto {

        private boolean success;
        private Long memberId;
        private Long contentId;

        public static ScrapResponseDto of(Scrap scrap) {
            return new ScrapResponseDto(true, scrap.getMember().getId(),scrap.getContent().getId());
        }
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ScrapDeleteResponseDto {
        private boolean success;
    }
}
