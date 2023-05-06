package com.list.nevrytime.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.list.nevrytime.entity.Image;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


public class ImageDto {

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ImageRequestDto {
        private Long contentId;
        private Long memberId;
    }


    @Getter
    @Setter
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    public static class ImageResponseDto {
        private Long imageId;
        private Long contentId;
        private String imageName;

        public static ImageResponseDto of(Image image) {
            return new ImageResponseDto(image.getId(), image.getContent().getId(), image.getImageName());
        }
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    public static class DownloadImageResponseDto {
        private byte[] file;
        private String type;
    }
}
