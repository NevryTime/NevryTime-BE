package com.list.nevrytime.service;

import com.list.nevrytime.exception.CustomException;
import com.list.nevrytime.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.list.nevrytime.dto.MemberDto.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {
    private final MemberRepository memberRepository;

    public MemberResponseDto findMemberInfoById(Long memberId) {
        return memberRepository.findById(memberId)
                .map(MemberResponseDto::of)
                .orElseThrow(() -> new CustomException(HttpStatus.BAD_REQUEST, "로그인 유저 정보가 없습니다."));
    }

    public MemberResponseDto findMemberInfoByName(String name) {
        return memberRepository.findByName(name)
                .map(MemberResponseDto::of)
                .orElseThrow(() -> new CustomException(HttpStatus.BAD_REQUEST, "유저 정보가 없습니다."));
    }
}
