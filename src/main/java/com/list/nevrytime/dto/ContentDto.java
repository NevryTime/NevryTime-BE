package com.list.nevrytime.dto;

import com.list.nevrytime.entity.Board;
import com.list.nevrytime.entity.Content;
import com.list.nevrytime.entity.Member;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;

public class ContentDto {

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ContentResponseDto {

        private Long id;
        private Board board;
        private Member member;
        private String title;
        private String content;
        private int likes;
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
        private LocalDateTime createAt;
        private boolean isImage;
        private boolean Show;

        public static ContentResponseDto of(Content content) {
            return new ContentResponseDto(content.getId(), content.getBoard(),content.getMember(),content.getTitle(),content.getContent(), content.getLikes(), content.getCreateAt(), content.isImage(), content.isShow());
        }
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public class ContentRequestDto {

        private Long id;
        private String title;
        private String content;
    }
}