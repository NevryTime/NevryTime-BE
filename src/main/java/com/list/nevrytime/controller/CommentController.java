package com.list.nevrytime.controller;

import com.list.nevrytime.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/{contentId}")
    public ResponseEntity<List<CommentResponseDto>> findComment(@PathVariable Long contentId) {
        return ResponseEntity.ok((commentService.findCommentByContentId(contentId)));
    }
}
