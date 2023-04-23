package com.list.nevrytime.controller;


import com.list.nevrytime.dto.MemberRequestDto;
import com.list.nevrytime.dto.MemberResponseDto;
import com.list.nevrytime.dto.TokenDto;
import com.list.nevrytime.dto.TokenRequestDto;
import com.list.nevrytime.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<TokenDto> login(@RequestBody MemberRequestDto memberRequestDto) {
        return ResponseEntity.ok(authService.login(memberRequestDto));
    }

    @PostMapping("/refresh")
    public ResponseEntity<TokenDto> reissue(@RequestBody TokenRequestDto tokenRequestDto) {
        return ResponseEntity.ok(authService.reissue(tokenRequestDto));
    }

    @PostMapping("/logout")
    public ResponseEntity<Boolean> logout(@RequestBody TokenRequestDto tokenRequestDto) {
        return ResponseEntity.ok(authService.logout(tokenRequestDto));
    }
}
