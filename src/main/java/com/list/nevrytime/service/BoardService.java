package com.list.nevrytime.service;

import com.list.nevrytime.dto.BoardResponseDto;
import com.list.nevrytime.entity.Board;
import com.list.nevrytime.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardService {

    private final BoardRepository boardRepository;

    @Transactional
    public BoardResponseDto createBoard(String boardName) {
        if (boardRepository.existsByName(boardName)) {
            throw new RuntimeException("이미 존재하는 게시판입니다");
        }
        Board board = new Board();
        board.setName(boardName);
        return BoardResponseDto.of(boardRepository.save(board));
    }

    public BoardResponseDto findBoardInfoByName(String name) {
        return boardRepository.findByName(name)
                .map(BoardResponseDto::of)
                .orElseThrow(() -> new RuntimeException("게시판 정보가 없습니다."));
    }
}
