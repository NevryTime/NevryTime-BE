package com.list.nevrytime.controller;

import com.list.nevrytime.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.list.nevrytime.dto.CommentDto.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comment")
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/create")
    public ResponseEntity<CommentResponseDto> createComment(@RequestBody CommentCreateRequestDto commentCreateRequestDto) {
        return ResponseEntity.ok(commentService.createComment(commentCreateRequestDto));
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<CommentDeleteResponseDto> deleteComment(@PathVariable Long commentId) {
        return ResponseEntity.ok(commentService.deleteComment(commentId));
    }
}
