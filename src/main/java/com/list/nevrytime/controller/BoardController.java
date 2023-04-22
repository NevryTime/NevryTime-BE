package com.list.nevrytime.controller;

import com.list.nevrytime.dto.BoardDto;
import com.list.nevrytime.dto.BoardResponseDto;
import com.list.nevrytime.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/board")
public class BoardController {

    private final BoardService boardService;

    @PostMapping("/create")
    public String createBoard(@RequestBody BoardDto boardDto) {
        return boardService.createBoard(boardDto.getName()).getName();
    }

    @PostMapping("/{name}")
    public ResponseEntity<BoardResponseDto> findBoardInfoByName(@PathVariable String name) {
        return ResponseEntity.ok(boardService.findBoardInfoByName(name));
    }
}
