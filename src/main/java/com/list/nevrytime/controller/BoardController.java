package com.list.nevrytime.controller;

import com.list.nevrytime.dto.BoardDto;
import com.list.nevrytime.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.list.nevrytime.dto.BoardDto.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/board")
public class BoardController {

    private final BoardService boardService;

    @GetMapping("/")
    public ResponseEntity<BoardListResponseDto> findAllBoard() {
        return ResponseEntity.ok(new BoardListResponseDto(true,boardService.findAllBoard()));
    }

    @PostMapping("/create")
    public ResponseEntity<BoardResponseDto> createBoard(@RequestBody BoardRequestDto boardRequestDto) {
        return ResponseEntity.ok(boardService.createBoard(boardRequestDto));
    }

    @PostMapping("/{name}")
    public ResponseEntity<BoardResponseDto> findBoardInfoByName(@PathVariable String name) {
        return ResponseEntity.ok(boardService.findBoardInfoByName(name));
    }
}
