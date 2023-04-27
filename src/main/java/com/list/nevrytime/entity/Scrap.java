package com.list.nevrytime.entity;

import lombok.*;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@Entity
@Table(name = "scrap")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Scrap {

    @Id
    @GeneratedValue
    @Column(name = "scrap_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "content_id")
    private Content content;
}