package com.list.nevrytime.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.engine.profile.Fetch;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "board")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Board {

    @Id
    @Column(name = "board_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @Column(name = "board_name")
    private String name;

    @JsonIgnore
    @Builder.Default
    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL)
    List<Content> contents = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private BoardType boardType;
}
