package com.list.nevrytime.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.list.nevrytime.entity.Content;
import com.list.nevrytime.entity.Image;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class ImageDto {

    @Getter
    @Setter
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    public static class ImageResponseDto {
        private Long imageId;
        private Long contentId;
        private String imageName;
        private String imagePath;

        public static ImageResponseDto of(Image image) {
            return new ImageResponseDto(image.getId(), image.getContent().getId(), image.getImageName(), image.getImagePath());
        }
    }
}
