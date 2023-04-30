package com.list.nevrytime.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
@AllArgsConstructor
@Builder
public class Content {

    @Id
    @Column(name = "content_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    private String title;
    private String content;
    private LocalDateTime createAt;

    @ColumnDefault("0")
    private int commentCount;
    @ColumnDefault("0")
    private int scraps;
    @ColumnDefault("0")
    private int likes;
    @ColumnDefault("false")
    private boolean isImage;
    @ColumnDefault("false")
    private boolean isShow;

    @JsonIgnore
    @OneToMany(mappedBy = "content", cascade = CascadeType.ALL)
    List<Comment> comments = new ArrayList<>();

    public void changeBoard(Board board) {
        this.board = board;
        board.getContents().add(this);
    }
}
