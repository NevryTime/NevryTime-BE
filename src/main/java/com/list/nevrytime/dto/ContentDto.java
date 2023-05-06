package com.list.nevrytime.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.list.nevrytime.entity.Content;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

import static com.list.nevrytime.dto.CommentDto.*;
import static com.list.nevrytime.dto.ImageDto.*;

public class ContentDto {

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DetailContentResponseDto {

        private ContentResponseDto contentResponseDto;
        private List<CommentResponseDto> commentList;
        private List<DownloadImageResponseDto> downloadImageResponseDtos;
        private boolean isWriter;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ContentResponseDto {

        private Long id;
        private String boardName;
        private String nickName;
        private String title;
        private String content;
        private int commentCount;
        private int imageCount;
        private int scraps;
        private int likes;
        private LocalDateTime createAt;
        private boolean isImage;
        private boolean isShow;

        public static ContentResponseDto of(Content content) {
            return new ContentResponseDto(content.getId(), content.getBoard().getName(),content.getMember().getNickName(),content.getTitle(),content.getContent(), content.getCommentCount(), content.getImageCount(), content.getScraps(), content.getLikes(), content.getCreateAt(), content.isImage(), content.isShow());
        }
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    public static class ContentWithImageResponseDto {
        private ContentResponseDto contentResponseDto;
        private List<ImageResponseDto> imageResponseDtos;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ContentCreateRequestDto {

        private String title;
        private String content;
        private Long boardId;
        @NonNull
        private boolean isImage;
        @NonNull
        private boolean isShow;

    }

    @Getter
    @Setter
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    public static class ContentListResponseDto {
        private boolean success;
        private List<ContentResponseDto> contentList;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
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
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    public static class ContentUpdateResponseDto {

        private boolean success;
        private ContentResponseDto contentResponseDto;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    public static class ContentPageResponseDto {

        private boolean success;
        private List<ContentResponseDto> contentPage;
        private int contentTotalPages;
        private Long contentTotalElements;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class MainContentsDto {
        private Long contentId;
        private Long boardId;
        private String title;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    public static class MainContentResponseDto {
        private Boolean success;
        private List<MainContentsDto> contents;

    }
}