package com.list.nevrytime.controller;

import com.list.nevrytime.security.jwt.MemberPrincipal;
import com.list.nevrytime.service.CommentService;
import lombok.RequiredArgsConstructor;
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
        return ResponseEntity.ok(commentService.createComment(memberPrincipal.getMember().getId(), commentCreateRequestDto));
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<CommentDeleteResponseDto> deleteComment(@AuthenticationPrincipal MemberPrincipal memberPrincipal,@PathVariable Long commentId) {
        return ResponseEntity.ok(commentService.deleteComment(memberPrincipal.getMember().getId(), commentId));
    }
}
