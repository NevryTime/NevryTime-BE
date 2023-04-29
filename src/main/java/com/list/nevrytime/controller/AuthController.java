package com.list.nevrytime.controller;

import com.list.nevrytime.dto.TokenDto.TokenRequestDto;
import com.list.nevrytime.entity.Member;
import com.list.nevrytime.exception.CustomException;
import com.list.nevrytime.security.jwt.MemberPrincipal;
import com.list.nevrytime.service.AuthService;
import com.list.nevrytime.service.JwtService;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.list.nevrytime.dto.MemberDto.*;
import static com.list.nevrytime.dto.TokenDto.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final JwtService jwtService;

    @PostMapping("/signup")
    public RegisterResponseDto register(@RequestBody @Valid RegisterRequestDto registerRequestDto) {
        Member member = authService.register(registerRequestDto);
        return new RegisterResponseDto(true, member.getName(), member.getNickName());
    }


    @PostMapping("/login")
    public LoginResponseDto login(@RequestBody @Valid LoginDto loginDto) {
        Long loginId = authService.login(loginDto);
        String accessToken = jwtService.createAccessToken(loginId);
        String refreshToken = jwtService.createRefreshToken(loginId);
        authService.saveRefreshToken(loginId, refreshToken);
        return new LoginResponseDto(true, loginId,accessToken, refreshToken);
    }

    @PostMapping("/refresh")
    public UseRefreshTokenResponseDto useRefreshToken(@RequestBody @Valid UseRefreshTokenRequestDto useRefreshTokenRequestDto) {
        Claims claims = jwtService.decodeJwtToken(useRefreshTokenRequestDto.getRefreshToken());

        if (!claims.getSubject().equals("refresh_token")) {
            throw new CustomException(HttpStatus.UNAUTHORIZED, "잘못된 요청입니다.");
        }

        if (authService.getRefreshToken(claims.get("uid", Long.class)) == null) {
            throw new CustomException(HttpStatus.UNAUTHORIZED, "잘못된 요청입니다.");
        }

        String accessToken = jwtService.createAccessToken(claims.get("uid", Long.class));

        return new UseRefreshTokenResponseDto(true, accessToken);
    }

    @PostMapping("/logout")
    public ResponseEntity<TokenDeleteDto> logout(@RequestBody TokenRequestDto tokenRequestDto) {
        return ResponseEntity.ok(authService.logout(tokenRequestDto));
    }

    @PutMapping("/password")
    public MemberResponseDto updatePassword(@AuthenticationPrincipal MemberPrincipal memberPrincipal, @RequestBody UpdatePasswordRequestDto updatePasswordRequestDto) {
        Member member = authService.updatePassword(memberPrincipal.getMember().getId(), updatePasswordRequestDto);
        return new MemberResponseDto(true, member.getName(), member.getNickName());
    }

    @PutMapping("/nickname")
    public MemberResponseDto updateNickname(@AuthenticationPrincipal MemberPrincipal memberPrincipal, @RequestBody UpdateNicknameRequestDto updateNicknameRequestDto) {
        Member member = authService.updateNickname(memberPrincipal.getMember().getId(), updateNicknameRequestDto);
        return new MemberResponseDto(true, member.getName(), member.getNickName());
    }

    @DeleteMapping("/withdrawal")
    public DeleteMemberResponseDto withdrawal(@AuthenticationPrincipal MemberPrincipal memberPrincipal, @RequestBody DeleteMemberRequestDto deleteMemberRequestDto) {
        Boolean result = authService.deleteMember(memberPrincipal.getMember().getId(), deleteMemberRequestDto.getPassword());

        return new DeleteMemberResponseDto(result);
    }

    @PostMapping("/test")
    public ResponseEntity<TokenDeleteDto> test() {
        return ResponseEntity.ok(authService.test());
    }
}
