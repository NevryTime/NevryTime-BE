package com.list.nevrytime.controller;

import com.list.nevrytime.entity.Member;
import com.list.nevrytime.exception.CustomException;
import com.list.nevrytime.security.jwt.MemberPrincipal;
import com.list.nevrytime.service.ContentService;
import com.list.nevrytime.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.list.nevrytime.dto.ContentDto.*;
import static com.list.nevrytime.dto.MemberDto.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/member")
public class MemberController {
    private final MemberService memberService;
    private final ContentService contentService;

    @GetMapping("/me")
    public MemberResponseDto findMemberInfoById(@AuthenticationPrincipal MemberPrincipal memberPrincipal) {
        Member member = memberService.findMemberInfoById(memberPrincipal.getMember().getId());
        System.out.println("member.getNickName() = " + member.getNickName());
        return new MemberResponseDto(true, member.getName(), member.getNickName());
    }

    @GetMapping("/{name}")
    public MemberResponseDto findMemberInfoByName(@PathVariable String name) {
        Member member = memberService.findMemberInfoByName(name);
        return new MemberResponseDto(true, member.getName(), member.getNickName());
    }

    @GetMapping("/me/content")
    public ContentListResponseDto findMyContent(@AuthenticationPrincipal MemberPrincipal memberPrincipal) {
        List<ContentResponseDto> contents = contentService.findContents(memberPrincipal.getMember().getId());
        if (contents.isEmpty()) {
            throw new CustomException(HttpStatus.BAD_REQUEST, "작성한 글이 없습니다.");
        }
        return new ContentListResponseDto(true, contents);
    }
}
