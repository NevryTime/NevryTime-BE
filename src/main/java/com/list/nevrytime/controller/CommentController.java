package com.list.nevrytime.controller;

import com.list.nevrytime.entity.Comment;
import com.list.nevrytime.exception.CustomException;
import com.list.nevrytime.security.jwt.MemberPrincipal;
import com.list.nevrytime.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import static com.list.nevrytime.dto.CommentDto.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comment")
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/create")
    public ResponseEntity<CommentResponseDto> createComment(@AuthenticationPrincipal MemberPrincipal memberPrincipal, @RequestBody CommentCreateRequestDto commentCreateRequestDto) {
        if (memberPrincipal.getMember().getId() == null) {
            throw new CustomException(HttpStatus.UNAUTHORIZED, "인증되지 않은 유저입니다.");
        }
        return ResponseEntity.ok(commentService.createComment(memberPrincipal.getMember().getId(), commentCreateRequestDto));
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<CommentDeleteResponseDto> deleteComment(@AuthenticationPrincipal MemberPrincipal memberPrincipal, @PathVariable Long commentId) {
        if (memberPrincipal.getMember().getId() == null) {
            throw new CustomException(HttpStatus.UNAUTHORIZED, "인증되지 않은 유저입니다.");
        }
        return ResponseEntity.ok(commentService.deleteComment(memberPrincipal.getMember().getId(), commentId));
    }

    @PutMapping("/{commentId}")
    public CommentResponseDto updateComment(@AuthenticationPrincipal MemberPrincipal memberPrincipal, @RequestBody UpdateCommentRequestDto updateCommentRequestDto) {
        if (memberPrincipal.getMember().getId() == null) {
            throw new CustomException(HttpStatus.UNAUTHORIZED, "인증되지 않은 유저입니다.");
        }
        Comment comment = commentService.updateComment(memberPrincipal.getMember().getId(), updateCommentRequestDto);
        return CommentResponseDto.of(comment);
    }
}
