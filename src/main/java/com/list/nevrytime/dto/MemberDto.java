package com.list.nevrytime.dto;

import com.list.nevrytime.entity.Authority;
import com.list.nevrytime.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

public class MemberDto {


    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class MemberRequestDto {
        private String name;
        private String password;

        public Member toMember(PasswordEncoder passwordEncoder) {
            return Member.builder()
                    .name(name)
                    .password(passwordEncoder.encode(password))
                    .authority(Authority.ROLE_USER)
                    .build();
        }

        public UsernamePasswordAuthenticationToken toAuthentication() {
            return new UsernamePasswordAuthenticationToken(name, password);
        }
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class MemberResponseDto {
        private String name;

        public static MemberResponseDto of(Member member) {
            return new MemberResponseDto(member.getName());
        }
    }

}
