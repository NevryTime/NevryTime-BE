package com.list.nevrytime.controller;

import com.list.nevrytime.service.HeartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.list.nevrytime.dto.HeartDto.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/heart")
public class HeartController {

    private final HeartService heartService;

    @PostMapping("/")
    public ResponseEntity<HeartResponseDto> insert(@RequestBody @Valid HeartRequestDto heartRequestDto) {
        return ResponseEntity.ok(heartService.insert(heartRequestDto));
    }

    @DeleteMapping("/")
    public ResponseEntity<HeartDeleteResponseDto> delete(@RequestBody @Valid HeartRequestDto heartRequestDto) {
        return ResponseEntity.ok(heartService.delete(heartRequestDto));
    }

}
