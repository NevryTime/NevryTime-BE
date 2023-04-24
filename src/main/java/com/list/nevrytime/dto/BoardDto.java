package com.list.nevrytime.dto;

import com.list.nevrytime.entity.Board;
import com.list.nevrytime.entity.BoardType;
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
        private BoardType boardType;

    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class BoardResponseDto {

        private Long id;
        private String name;
        private BoardType boardType;

        public static BoardResponseDto of(Board board) {
            return new BoardResponseDto(board.getId(), board.getName(), board.getBoardType());
        }
    }
}
