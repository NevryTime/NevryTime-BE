package com.list.nevrytime.dto;

import com.list.nevrytime.entity.Board;
import com.list.nevrytime.entity.Content;
import com.list.nevrytime.entity.Member;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import java.time.LocalDateTime;

public class ContentDto {

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ContentResponseDto {

        private Long id;
        private String boardName;
        private String userName;
        private String title;
        private String content;
        private int likes;
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
        private LocalDateTime createAt;
        private boolean isImage;
        private boolean isShow;

        public static ContentResponseDto of(Content content) {
            return new ContentResponseDto(content.getId(), content.getBoard().getName(),content.getMember().getName(),content.getTitle(),content.getContent(), content.getLikes(), content.getCreateAt(), content.isImage(), content.isShow());
        }
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ContentCreateRequestDto {

        private String title;
        private String content;
        private Long boardId;
        private Long memberId;
        @Nullable
        private boolean isImage;
        @Nullable
        private boolean isShow;
    }

}