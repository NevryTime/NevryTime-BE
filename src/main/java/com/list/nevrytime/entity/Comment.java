package com.list.nevrytime.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "comment")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Comment {

    @Id
    @Column(name = "comment_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "content_id")
    private Content content;

    @OneToOne
    @JoinColumn(name = "member_id")
    private Member member;

    private String commentContent;

    @ColumnDefault("0")
    private Long parentId;
    @ColumnDefault("0")
    private int depth;
    @ColumnDefault("false")
    private boolean isDeleted;


    private LocalDateTime createAt;


}
