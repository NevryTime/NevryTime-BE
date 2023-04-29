package com.list.nevrytime.security.jwt;

import com.list.nevrytime.entity.Member;
import lombok.Getter;
import org.springframework.security.core.userdetails.User;

import java.io.Serializable;

@Getter
public class MemberPrincipal extends User implements Serializable {

    private final Member member;

    public MemberPrincipal(Member member) {
        super(member.getId().toString(), member.getId().toString(), member.getAuthorities());
        this.member = member;
    }

}
