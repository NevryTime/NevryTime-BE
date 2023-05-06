package com.list.nevrytime.controller;

import com.list.nevrytime.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static com.list.nevrytime.dto.ImageDto.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/image")
public class ImageController {

    final private ImageService imageService;

    // 업로드
    @PostMapping
    public ImageResponseDto uploadImage(@RequestPart(value = "image", required = false) MultipartFile file, @RequestPart(value = "imageRequestDto") ImageRequestDto imageRequestDto) throws IOException {
        return imageService.uploadImage(file, imageRequestDto.getContentId());
    }

    // 다운로드
    @GetMapping("/{fileId}")
    public ResponseEntity<?> downloadImage(@PathVariable("fileId") Long fileId) {
        DownloadImageResponseDto downloadImageResponseDto = imageService.downloadImage(fileId);
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.valueOf(downloadImageResponseDto.getType()))//"image/png"
                .body(downloadImageResponseDto.getFile());
    }

}
