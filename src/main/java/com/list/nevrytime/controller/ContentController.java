package com.list.nevrytime.controller;

import com.list.nevrytime.dto.ContentDto.ContentCreateRequestDto;
import com.list.nevrytime.exception.CustomException;
import com.list.nevrytime.security.jwt.MemberPrincipal;
import com.list.nevrytime.service.ContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    public ResponseEntity<ContentWithCommentResponseDto> findContentById(@PathVariable Long contentId) {
        return ResponseEntity.ok(contentService.findContentById(contentId));
    }

    @DeleteMapping("/{contentId}")
    public ResponseEntity<ContentDeleteResponseDto> deleteContentByName(@AuthenticationPrincipal MemberPrincipal memberPrincipal,@PathVariable Long contentId ) {
        if (memberPrincipal.getMember().getId() == null) {
            throw new CustomException(HttpStatus.UNAUTHORIZED, "인증되지 않은 유저입니다.");
        }
        return ResponseEntity.ok(contentService.deleteContentByName(memberPrincipal.getMember().getId(), contentId));
    }

    @PutMapping("/{contentId}")
    public ResponseEntity<ContentUpdateResponseDto> updateContent(@PathVariable Long contentId, @RequestBody ContentUpdateRequestDto contentUpdateRequestDto) {
        return ResponseEntity.ok(contentService.update(contentId, contentUpdateRequestDto));
    }

    @GetMapping("/{boardId}/p/{page}")
    public ResponseEntity<ContentPageResponseDto> pageContent(@PathVariable Long boardId, @PathVariable int page, @RequestParam int length) {
        return ResponseEntity.ok(contentService.pageContent(boardId, page, length));
    }

    @PostMapping("/create")
    public ResponseEntity<ContentResponseDto> createContent(@AuthenticationPrincipal MemberPrincipal memberPrincipal, @RequestBody ContentCreateRequestDto contentCreateRequestDto) {
        if (memberPrincipal.getMember().getId() == null) {
            throw new CustomException(HttpStatus.UNAUTHORIZED, "인증되지 않은 유저입니다.");
        }
        return ResponseEntity.ok(contentService.createContent(memberPrincipal.getMember().getId(), contentCreateRequestDto));
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

    @GetMapping("/main")
    public MainContentResponseDto mainContent() {
        return contentService.getMainContents();
    }
}
