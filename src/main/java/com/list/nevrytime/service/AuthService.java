package com.list.nevrytime.service;


import com.list.nevrytime.dto.TokenDto.TokenRequestDto;
import com.list.nevrytime.entity.Member;
import com.list.nevrytime.entity.RefreshToken;
import com.list.nevrytime.exception.CustomException;
import com.list.nevrytime.security.jwt.TokenProvider;
import com.list.nevrytime.repository.MemberRepository;
import com.list.nevrytime.repository.RefreshTokenRepository;
import com.list.nevrytime.security.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
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
    private final TokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    @Transactional
    public MemberResponseDto signup(MemberRequestDto memberRequestDto) {
        if (memberRepository.existsByName(memberRequestDto.getName())) {
            throw new CustomException(HttpStatus.BAD_REQUEST, "이미 가입되어 있는 유저입니다");
        }

        Member member = memberRequestDto.toMember(passwordEncoder);
        return MemberResponseDto.of(memberRepository.save(member));
    }

    @Transactional
    public TokenResponseDto login(MemberRequestDto memberRequestDto) {
        // 1. Login ID/PW 를 기반으로 AuthenticationToken 생성
        UsernamePasswordAuthenticationToken authenticationToken = memberRequestDto.toAuthentication();

        // 2. 실제로 검증 (사용자 비밀번호 체크) 이 이루어지는 부분
        //    authenticate 메서드가 실행이 될 때 CustomUserDetailsService 에서 만들었던 loadUserByUsername 메서드가 실행됨
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        // 3. 인증 정보를 기반으로 JWT 토큰 생성
        TokenResponseDto tokenResponseDto = tokenProvider.generateTokenDto(authentication);

        // 4. RefreshToken 저장
        RefreshToken refreshToken = RefreshToken.builder()
                .key(authentication.getName())
                .value(tokenResponseDto.getRefreshToken())
                .build();

        refreshTokenRepository.save(refreshToken);

        // 5. 토큰 발급
        return tokenResponseDto;
    }

    @Transactional
    public TokenResponseDto reissue(TokenRequestDto tokenRequestDto) {
        // 1. Refresh Token 검증
        if (!tokenProvider.validateToken(tokenRequestDto.getRefreshToken())) {
            throw new CustomException(HttpStatus.BAD_REQUEST, "Refresh Token 이 유효하지 않습니다.");
        }

        // 2. Access Token 에서 Member ID 가져오기
        Authentication authentication = tokenProvider.getAuthentication(tokenRequestDto.getAccessToken());

        // 3. 저장소에서 Member ID 를 기반으로 Refresh Token 값 가져옴
        RefreshToken refreshToken = refreshTokenRepository.findByKey(authentication.getName())
                .orElseThrow(() -> new CustomException(HttpStatus.BAD_REQUEST, "로그아웃 된 사용자입니다."));

        // 4. Refresh Token 일치하는지 검사
        if (!refreshToken.getValue().equals(tokenRequestDto.getRefreshToken())) {
            throw new CustomException(HttpStatus.BAD_REQUEST, "토큰의 유저 정보가 일치하지 않습니다.");
        }

        // 5. 새로운 토큰 생성
        TokenResponseDto tokenResponseDto = tokenProvider.generateTokenDto(authentication);

        // 6. 저장소 정보 업데이트
        RefreshToken newRefreshToken = refreshToken.updateValue(tokenResponseDto.getRefreshToken());
        refreshTokenRepository.save(newRefreshToken);

        // 토큰 발급
        return tokenResponseDto;
    }

    @Transactional
    public TokenDeleteDto logout(TokenRequestDto tokenRequestDto) {
        // 1. Refresh Token 검증
        if (!tokenProvider.validateToken(tokenRequestDto.getRefreshToken())) {
            throw new CustomException(HttpStatus.BAD_REQUEST, "Refresh Token 이 유효하지 않습니다.");
        }

        // 2. Access Token 에서 Member ID 가져오기
        Authentication authentication = tokenProvider.getAuthentication(tokenRequestDto.getAccessToken());

        refreshTokenRepository.deleteByKey(authentication.getName());
        return new TokenDeleteDto(true);
    }

    @Transactional
    public TokenDeleteDto test() {
        memberRepository.findById(SecurityUtil.getCurrentMemberId())
                .orElseThrow( () -> new CustomException(HttpStatus.BAD_REQUEST, "토큰이 유효하지 않습니다."));
        return new TokenDeleteDto(true);
    }
}
