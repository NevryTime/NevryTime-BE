package com.list.nevrytime.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.engine.profile.Fetch;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "board")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
public class Board {

    @Id
    @Column(name = "board_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "board_name")
    private String name;

    @OneToMany(mappedBy = "board")
    List<Content> contents = new ArrayList<>();
}
