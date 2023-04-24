package com.list.nevrytime.entity;

import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "content")
@Getter
@Setter
@DynamicInsert
@NoArgsConstructor
public class Content {

    @Id
    @Column(name = "content_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "content_id")
    private Member member;

    private String title;
    private String content;
    private LocalDateTime createAt;

    @ColumnDefault(value = "0")
    private int likes;
    @ColumnDefault(value = "false")
    private boolean isImage;
    @ColumnDefault(value = "false")
    private boolean isShow;

    public void changeBoard(Board board) {
        this.board = board;
        board.getContents().add(this);
    }
}
