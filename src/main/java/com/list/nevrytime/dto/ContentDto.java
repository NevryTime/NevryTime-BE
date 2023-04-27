package com.list.nevrytime.dto;

import com.list.nevrytime.entity.Content;
import lombok.*;
import org.springframework.lang.Nullable;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.list.nevrytime.dto.CommentDto.*;

public class ContentDto {

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ContentWithCommentResponseDto {

        private ContentResponseDto contentResponseDto;
        private List<CommentResponseDto> commentList;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ContentResponseDto {

        private Long id;
        private String boardName;
        private String memberName;
        private String title;
        private String content;
        private int commentCount;
        private int hearts;
        private int likes;
        private LocalDateTime createAt;
        private boolean isImage;
        private boolean isShow;

        public static ContentResponseDto of(Content content) {
            return new ContentResponseDto(content.getId(), content.getBoard().getName(),content.getMember().getName(),content.getTitle(),content.getContent(), content.getCommentCount(), content.getHearts(), content.getLikes(), content.getCreateAt(), content.isImage(), content.isShow());
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

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ContentListResponseDto {
        private boolean success;
        private List<ContentResponseDto> contentList = new ArrayList<>();
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ContentDeleteResponseDto {
        private boolean success;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ContentUpdateRequestDto {

        private String title;
        private String content;
        private Boolean isImage;
        private Boolean isShow;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ContentUpdateResponseDto {

        private boolean success;
        private ContentResponseDto contentResponseDto;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ContentPageResponseDto {

        private boolean success;
        private List<ContentResponseDto> contentPage;
        private int contentTotalPages;
        private Long contentTotalElements;
    }
}