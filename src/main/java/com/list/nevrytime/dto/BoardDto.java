package com.list.nevrytime.dto;

import com.list.nevrytime.entity.Board;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class BoardDto {

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class BoardRequestDto{

        private String name;

    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class BoardResponseDto {

        private Long id;
        private String name;

        public static BoardResponseDto of(Board board) {
            return new BoardResponseDto(board.getId(), board.getName());
        }
    }
}
