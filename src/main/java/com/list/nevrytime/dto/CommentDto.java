package com.list.nevrytime.dto;

import com.list.nevrytime.entity.Comment;
import lombok.*;

import java.time.LocalDateTime;

public class CommentDto {

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CommentResponseDto {

        private Long id;
        private Long contentId;
        private String nickName;
        private String commentContent;
        private Long parentId;
        private int depth;
        private boolean isDeleted;
        private LocalDateTime createAt;
        private boolean isShow;

        public static CommentResponseDto of(Comment comment) {
            return new CommentResponseDto(comment.getId(),comment.getMember().getId(), comment.getMember().getNickName(), comment.getCommentContent(), comment.getParentId(), comment.getDepth(), comment.isDeleted(), comment.getCreateAt(), comment.isShow());
        }
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CommentCreateRequestDto {
        private Long contentId;
        private String commentContent;
        private Long parentId;
        private int depth;
        private boolean isShow;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CommentDeleteResponseDto {
        private boolean success;
        private Long commentId;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UpdateCommentRequestDto {
        private String commentContent;
        private boolean isShow;
    }
}
