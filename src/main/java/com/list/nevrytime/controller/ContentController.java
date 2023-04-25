package com.list.nevrytime.controller;

import com.list.nevrytime.dto.ContentDto.ContentCreateRequestDto;
import com.list.nevrytime.service.ContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.list.nevrytime.dto.ContentDto.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/content")
public class ContentController {

    private final ContentService contentService;

    @GetMapping("/")
    public ResponseEntity<ContentListResponseDto> findAllContent() {
        return ResponseEntity.ok(new ContentListResponseDto(true,contentService.findAllContent()));
    }

    @GetMapping("/{contentId}")
    public ResponseEntity<ContentResponseDto> findContentById(@PathVariable Long contentId) {
        return ResponseEntity.ok(contentService.findContentById(contentId));
    }

    @DeleteMapping("/{contentId}")
    public ResponseEntity<ContentDeleteResponseDto> deleteContentByName(@PathVariable Long contentId ) {
        return ResponseEntity.ok(contentService.deleteContentByName(contentId));
    }

    @PutMapping("/{contentId}")
    public ResponseEntity<ContentUpdateResponseDto> updateContent(@PathVariable Long contentId, @RequestBody ContentUpdateRequestDto contentUpdateRequestDto) {
        return ResponseEntity.ok(contentService.update(contentId, contentUpdateRequestDto));
    }

    @GetMapping("/{boardId}/p/{page}")
    public ResponseEntity<ContentPageResponseDto> pageContent(@PathVariable Long boardId, @PathVariable int page, @RequestBody int length) {
        return ResponseEntity.ok(contentService.pageContent(boardId, page, length));
    }

    @PostMapping("/create")
    public ResponseEntity<ContentResponseDto> createContent(@RequestBody ContentCreateRequestDto contentCreateRequestDto) {
        return ResponseEntity.ok(contentService.createContent(contentCreateRequestDto));
    }

    @GetMapping("/popular")
    public ResponseEntity<ContentListResponseDto> liveBestContent() {
        return ResponseEntity.ok(new ContentListResponseDto(true, contentService.popularContent()));
    }

    @GetMapping("/hot/p/{page}")
    public ResponseEntity<ContentPageResponseDto> hotContent(@PathVariable int page) {
        return ResponseEntity.ok(contentService.hotContent(page));
    }

    @GetMapping("/hot4")
    public ResponseEntity<ContentListResponseDto> hotListContent() {
        return ResponseEntity.ok(new ContentListResponseDto(true, contentService.hotListContent()));
    }

    @GetMapping("/best/p/{page}")
    public ResponseEntity<ContentPageResponseDto> bestContent(@PathVariable int page) {
        return ResponseEntity.ok(contentService.bestContent(page));
    }
}
