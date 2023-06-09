package com.list.nevrytime.controller;

import com.list.nevrytime.dto.ContentDto.ContentCreateRequestDto;
import com.list.nevrytime.exception.CustomException;
import com.list.nevrytime.security.jwt.MemberPrincipal;
import com.list.nevrytime.service.ContentService;
import com.list.nevrytime.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.list.nevrytime.dto.ContentDto.*;
import static com.list.nevrytime.dto.ImageDto.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/content")
public class ContentController {

    private final ContentService contentService;
    private final ImageService imageService;

    @GetMapping("/")
    public ResponseEntity<ContentListResponseDto> findAllContent() {
        return ResponseEntity.ok(new ContentListResponseDto(true,contentService.findAllContent()));
    }

    @GetMapping("/{contentId}")
    public ResponseEntity<DetailContentResponseDto> findContentById(@PathVariable Long contentId, @AuthenticationPrincipal MemberPrincipal memberPrincipal) {
        DetailContentResponseDto contentById = contentService.findContentById(memberPrincipal.getMember().getId(), contentId);
        return ResponseEntity.ok(contentById);
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
    public ContentWithImageResponseDto createContent(@AuthenticationPrincipal MemberPrincipal memberPrincipal
            , @RequestPart(value = "contentCreateRequestDto") ContentCreateRequestDto contentCreateRequestDto
            , @RequestPart(value = "file", required = false) List<MultipartFile> multipartFile
    ) throws IOException {
        if (memberPrincipal.getMember().getId() == null) {
            throw new CustomException(HttpStatus.UNAUTHORIZED, "인증되지 않은 유저입니다.");
        }
            ContentResponseDto contentResponseDto = contentService.createContent(memberPrincipal.getMember().getId(), contentCreateRequestDto);

        if (multipartFile == null && !contentResponseDto.isImage()) {
            return new ContentWithImageResponseDto(contentResponseDto, null);
        }

        List<ImageResponseDto> imageResponseDtos = new ArrayList<>();
        for (MultipartFile file : multipartFile) {
            ImageResponseDto imageResponseDto = imageService.uploadImage(file, contentResponseDto.getId());
            imageResponseDtos.add(imageResponseDto);
        }

        return new ContentWithImageResponseDto(contentResponseDto, imageResponseDtos);
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
