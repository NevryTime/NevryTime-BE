package com.list.nevrytime.controller;

import com.list.nevrytime.dto.BoardDto;
import com.list.nevrytime.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.list.nevrytime.dto.BoardDto.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/board")
public class BoardController {

    private final BoardService boardService;

    @PostMapping("/create")
    public ResponseEntity<BoardResponseDto> createBoard(@RequestBody BoardResponseDto boardResponseDto) {
        return ResponseEntity.ok(boardService.createBoard(boardResponseDto.getName()));
    }

    @PostMapping("/{name}")
    public ResponseEntity<BoardResponseDto> findBoardInfoByName(@PathVariable String name) {
        return ResponseEntity.ok(boardService.findBoardInfoByName(name));
    }
}
