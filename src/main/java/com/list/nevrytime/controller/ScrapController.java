package com.list.nevrytime.controller;

import com.list.nevrytime.service.ScrapService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.list.nevrytime.dto.ScrapDto.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/scrap")
public class ScrapController {

    private final ScrapService scrapService;

    @PostMapping("/")
    public ResponseEntity<ScrapResponseDto> insert(@RequestBody @Valid ScrapRequestDto scrapRequestDto) {
        return ResponseEntity.ok(scrapService.insert(scrapRequestDto));
    }

    @DeleteMapping("/")
    public ResponseEntity<ScrapDeleteResponseDto> delete(@RequestBody @Valid ScrapRequestDto scrapRequestDto) {
        return ResponseEntity.ok(scrapService.delete(scrapRequestDto));
    }

}
