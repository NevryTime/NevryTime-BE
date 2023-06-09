package com.list.nevrytime.service;

import com.list.nevrytime.dto.BoardDto;
import com.list.nevrytime.entity.Board;
import com.list.nevrytime.exception.CustomException;
import com.list.nevrytime.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.list.nevrytime.dto.BoardDto.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardService {

    private final ModelMapper modelMapper;
    private final BoardRepository boardRepository;

    @Transactional
    public List<BoardResponseDto> findAllBoard() {
        List<Board> boards = boardRepository.findAll();
        List<BoardResponseDto> resultList = boards
                .stream()
                .map(post -> modelMapper.map(post, BoardResponseDto.class))
                .collect(Collectors.toList());
        return resultList;

    }

    @Transactional
    public BoardResponseDto createBoard(BoardRequestDto boardRequestDto) {
        if (boardRepository.existsByName(boardRequestDto.getName())) {
            throw new CustomException(HttpStatus.BAD_REQUEST, "이미 존재하는 게시판입니다");
        }

        Board board = Board.builder()
                .name(boardRequestDto.getName())
                .boardType(boardRequestDto.getBoardType())
                .build();

        return BoardResponseDto.of(boardRepository.save(board));
    }

    public BoardResponseDto findBoardInfoByName(String name) {
        return boardRepository.findByName(name)
                .map(BoardResponseDto::of)
                .orElseThrow(() -> new CustomException(HttpStatus.BAD_REQUEST, "게시판 정보가 없습니다."));
    }
}
