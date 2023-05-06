package com.list.nevrytime.service;

import com.list.nevrytime.entity.Content;
import com.list.nevrytime.entity.Image;
import com.list.nevrytime.exception.CustomException;
import com.list.nevrytime.repository.ContentRepository;
import com.list.nevrytime.repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;

import static com.list.nevrytime.dto.ImageDto.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class ImageService {


    private final ImageRepository imageRepository;
    private final ContentRepository contentRepository;

    public ImageResponseDto uploadImage(MultipartFile file, Long contentId) throws IOException {
        Content content = contentRepository.findById(contentId)
                .orElseThrow(() -> new CustomException(HttpStatus.BAD_REQUEST, "게시글 번호에 문제가 있습니다."));

        Image imageData = imageRepository.save(
                Image.builder()
                        .imageName(file.getOriginalFilename())
                        .content(content)
                        .type(file.getContentType())
                        .imageData(Base64.getEncoder().encodeToString(file.getBytes()))
                        .fileSize(file.getSize())
                        .build());

        content.setImageCount(content.getImageCount() + 1);
        contentRepository.save(content);

        return ImageResponseDto.of(imageData);
    }

    // 이미지 파일로 압축하기
    public DownloadImageResponseDto downloadImage(Long fileId) {
        Image imageData = imageRepository.findById(fileId)
                .orElseThrow(RuntimeException::new);

        byte[] file = Base64.getDecoder().decode(imageData.getImageData());
        String type = imageData.getType();
        return new DownloadImageResponseDto(file, type);
    }
}
