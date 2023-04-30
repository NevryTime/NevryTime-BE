package com.list.nevrytime.controller;

import com.list.nevrytime.exception.CustomException;
import com.list.nevrytime.security.jwt.MemberPrincipal;
import com.list.nevrytime.service.HeartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.list.nevrytime.dto.HeartDto.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/heart")
public class HeartController {

    private final HeartService heartService;

    @PostMapping("/")
    public ResponseEntity<HeartResponseDto> insert(@AuthenticationPrincipal MemberPrincipal memberPrincipal, @RequestBody HeartRequestDto heartRequestDto) {
        if (memberPrincipal.getMember().getId() == null) {
            throw new CustomException(HttpStatus.UNAUTHORIZED, "인증되지 않은 유저입니다.");
        }
        return ResponseEntity.ok(heartService.insert(memberPrincipal.getMember().getId(), heartRequestDto));
    }

    @DeleteMapping("/")
    public ResponseEntity<HeartDeleteResponseDto> delete(@AuthenticationPrincipal MemberPrincipal memberPrincipal, @RequestBody HeartRequestDto heartRequestDto) {
        if (memberPrincipal.getMember().getId() == null) {
            throw new CustomException(HttpStatus.UNAUTHORIZED, "인증되지 않은 유저입니다.");
        }
        return ResponseEntity.ok(heartService.delete(memberPrincipal.getMember().getId(), heartRequestDto));
    }

}
