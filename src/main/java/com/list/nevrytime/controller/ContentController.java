package com.list.nevrytime.controller;

import com.list.nevrytime.dto.BoardDto;
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

    @DeleteMapping("/{contentId}")
    public ResponseEntity<ContentDeleteResponseDto> deleteContentByName(@PathVariable Long contentId ) {
        return ResponseEntity.ok(contentService.deleteContentByName(contentId));
    }
    @PostMapping("/create")
    public ResponseEntity<ContentResponseDto> createContent(@RequestBody ContentCreateRequestDto contentCreateRequestDto) {
        return ResponseEntity.ok(contentService.createContent(contentCreateRequestDto));
    }
}
