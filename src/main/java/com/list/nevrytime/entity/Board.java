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
@NoArgsConstructor
@Getter
@Setter
public class Board {

    @Id
    @Column(name = "board_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "board_name")
    private String name;

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL)
    List<Content> contents = new ArrayList<>();
}
