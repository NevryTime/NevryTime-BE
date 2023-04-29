package com.list.nevrytime.controller;

import com.list.nevrytime.entity.Member;
import com.list.nevrytime.security.jwt.MemberPrincipal;
import com.list.nevrytime.service.MemberService;
import com.list.nevrytime.security.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import static com.list.nevrytime.dto.MemberDto.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/member")
public class MemberController {
    private final MemberService memberService;

    @GetMapping("/me")
    public MemberResponseDto findMemberInfoById(@AuthenticationPrincipal MemberPrincipal memberPrincipal) {
        Member member = memberService.findMemberInfoById(memberPrincipal.getMember().getId());
        System.out.println("member.getNickName() = " + member.getNickName());
        return new MemberResponseDto(true, member.getName(), member.getNickName());
    }

    @GetMapping("/{name}")
    public MemberResponseDto findMemberInfoByName(@PathVariable String name) {
        Member member = memberService.findMemberInfoByName(name);
        return new MemberResponseDto(true , member.getName(), member.getNickName());
    }
}
