package com.list.nevrytime.controller;


import com.list.nevrytime.dto.MemberDto;
import com.list.nevrytime.dto.TokenDto;
import com.list.nevrytime.dto.TokenDto.TokenRequestDto;
import com.list.nevrytime.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.list.nevrytime.dto.MemberDto.*;
import static com.list.nevrytime.dto.TokenDto.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<MemberResponseDto> signup(@RequestBody MemberRequestDto memberRequestDto) {
        return ResponseEntity.ok(authService.signup(memberRequestDto));
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponseDto> login(@RequestBody MemberRequestDto memberRequestDto) {
        return ResponseEntity.ok(authService.login(memberRequestDto));
    }

    @PostMapping("/refresh")
    public ResponseEntity<TokenResponseDto> reissue(@RequestBody TokenRequestDto tokenRequestDto) {
        return ResponseEntity.ok(authService.reissue(tokenRequestDto));
    }

    @PostMapping("/logout")
    public ResponseEntity<TokenDeleteDto> logout(@RequestBody TokenRequestDto tokenRequestDto) {
        return ResponseEntity.ok(authService.logout(tokenRequestDto));
    }

    @PostMapping("/test")
    public ResponseEntity<Boolean> test() {
        return ResponseEntity.ok(authService.test());
    }
}
