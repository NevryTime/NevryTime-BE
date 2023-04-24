package com.list.nevrytime.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "member")
@Entity
public class Member {

    @Id
    @Column(name = "member_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String nickName;

//    private String email;

    private String password;

    @Enumerated(EnumType.STRING)
    private Authority authority;

//    @JsonIgnore
//    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
//    @JoinColumn(name = "member_id")
//    private Content content;

    @Builder
    public Member(String name, String password, Authority authority) {
        this.name = name;
        this.password = password;
        this.authority = authority;
    }
}
