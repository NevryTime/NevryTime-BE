package com.list.nevrytime.controller;

import com.list.nevrytime.service.MemberService;
import com.list.nevrytime.security.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.list.nevrytime.dto.MemberDto.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/member")
public class MemberController {
    private final MemberService memberService;

    @GetMapping("/me")
    public ResponseEntity<MemberResponseDto> findMemberInfoById(@RequestBody Long memberId) {
        return ResponseEntity.ok(new MemberResponseDto(true,memberService.findMemberInfoById(memberId).getName()));
    }

    @GetMapping("/{name}")
    public ResponseEntity<MemberResponseDto> findMemberInfoByName(@PathVariable String name) {
        return ResponseEntity.ok(new MemberResponseDto(true ,memberService.findMemberInfoByName(name).getName()));
    }
}
