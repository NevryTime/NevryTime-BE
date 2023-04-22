package com.list.nevrytime.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "content")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Content {

    @Id
    @Column(name = "content_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String content;
    private int likes;
    private LocalDateTime createAt;
    private boolean isContent;
    private boolean isShow;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    public void changeBoard(Board board) {
        this.board = board;
        board.getContents().add(this);
    }
}
