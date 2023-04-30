package com.list.nevrytime.service;


import com.list.nevrytime.dto.TokenDto.TokenRequestDto;
import com.list.nevrytime.entity.Member;
import com.list.nevrytime.entity.RefreshToken;
import com.list.nevrytime.exception.CustomException;
import com.list.nevrytime.repository.MemberRepository;
import com.list.nevrytime.repository.RefreshTokenRepository;
import com.list.nevrytime.security.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.list.nevrytime.dto.MemberDto.*;
import static com.list.nevrytime.dto.TokenDto.*;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final RefreshTokenRepository refreshTokenRepository;

    @Transactional
    public Member register(RegisterRequestDto registerRequestDto) {
        if (memberRepository.existsByName(registerRequestDto.getName())) {
            throw new CustomException(HttpStatus.BAD_REQUEST, "이미 가입되어있는 아이디 입니다.");
        }

        Member member = Member.builder()
                .name(registerRequestDto.getName())
                .password(passwordEncoder.encode(registerRequestDto.getPassword()))
                .nickName(registerRequestDto.getNickName())
                .isBan(false)
                .build();
        return memberRepository.save(member);
    }

    @Transactional
    public Long login(LoginDto login) {
        Member member = memberRepository.findByName(login.getName())
                .orElseThrow(
                        () -> new CustomException(HttpStatus.UNAUTHORIZED, "아이디 또는 비밀번호가 일치하지 않습니다."));

        if (!passwordEncoder.matches(login.getPassword(), member.getPassword())) {
            throw new CustomException(HttpStatus.UNAUTHORIZED, "아이디 또는 비밀번호가 일치하지 않습니다.");
        }

        return member.getId();
    }

    @Transactional
    public Boolean saveRefreshToken(Long loginId, String refreshToken) {
        RefreshToken rt = RefreshToken.builder()
                .key(loginId.toString())
                .value(refreshToken)
                .build();
        refreshTokenRepository.save(rt);

        return true;
    }

    public String getRefreshToken(Long uid) {
        RefreshToken refreshToken = refreshTokenRepository.findByKey(uid.toString())
                .orElseThrow(() -> new CustomException(HttpStatus.BAD_REQUEST, "로그아웃 된 사용자입니다."));
        return refreshToken.toString();
    }

    @Transactional
    public Boolean logout(Long uid) {
        refreshTokenRepository.deleteByKey(uid.toString());
        return true;
    }

    public Member updatePassword(Long uid, UpdatePasswordRequestDto updatePasswordRequestDto) {
        Member member = memberRepository.findById(uid)
                .orElseThrow(
                        () -> new CustomException(HttpStatus.UNAUTHORIZED, "로그인이 필요합니다."));
        if (!passwordEncoder.matches(updatePasswordRequestDto.getRawPassword(), member.getPassword())) {
            throw new CustomException(HttpStatus.UNAUTHORIZED, "비밀번호가 일치하지 않습니다.");
        }

        String encodePassword = passwordEncoder.encode(updatePasswordRequestDto.getNewPassword());
        member.setPassword(encodePassword);

        return memberRepository.save(member);
    }

    public Member updateNickname(Long uid, UpdateNicknameRequestDto updateNicknameRequestDto) {
        Member member = memberRepository.findById(uid)
                .orElseThrow(
                        () -> new CustomException(HttpStatus.UNAUTHORIZED, "로그인이 필요합니다."));

        member.setNickName(updateNicknameRequestDto.getNickName());
        return memberRepository.save(member);
    }

    @Transactional
    public Boolean deleteMember(Long uid, String password) {
        Member member = memberRepository.findById(uid)
                .orElseThrow(
                        () -> new CustomException(HttpStatus.UNAUTHORIZED, "로그인이 필요합니다."));
        if (!passwordEncoder.matches(password, member.getPassword())) {
            throw new CustomException(HttpStatus.UNAUTHORIZED, "비밀번호가 일치하지 않습니다.");
        }

        memberRepository.deleteById(uid);
        refreshTokenRepository.deleteByKey(uid.toString());
        return true;
    }

    @Transactional
    public TokenDeleteDto test() {
        memberRepository.findById(SecurityUtil.getCurrentMemberId())
                .orElseThrow( () -> new CustomException(HttpStatus.BAD_REQUEST, "토큰이 유효하지 않습니다."));
        return new TokenDeleteDto(true);
    }
}
