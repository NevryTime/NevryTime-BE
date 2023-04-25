package com.list.nevrytime.entity;

import lombok.*;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@Entity
@Table(name = "heart")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Heart {

    @Id @GeneratedValue
    @Column(name = "heart_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "content_id")
    private Content content;
}
