package com.list.nevrytime.service;

import com.list.nevrytime.dto.BoardDto;
import com.list.nevrytime.dto.ContentDto;
import com.list.nevrytime.dto.ContentDto.ContentCreateRequestDto;
import com.list.nevrytime.entity.Board;
import com.list.nevrytime.entity.Content;
import com.list.nevrytime.entity.Member;
import com.list.nevrytime.repository.BoardRepository;
import com.list.nevrytime.repository.ContentRepository;
import com.list.nevrytime.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static com.list.nevrytime.dto.ContentDto.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ContentService {

    private final ModelMapper modelMapper;
    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;
    private final ContentRepository contentRepository;

    @Transactional
    public ContentResponseDto createContent(ContentCreateRequestDto contentCreateRequestDto) {
        Board board = boardRepository.findById(contentCreateRequestDto.getBoardId()).orElseThrow(
                () -> new RuntimeException("boardId가 유효하지 않습니다."));

        Member member = memberRepository.findById(contentCreateRequestDto.getMemberId()).orElseThrow(
                () -> new RuntimeException("memberId가 유효하지 않습니다."));

        Content content = Content.builder()
                .board(board)
                .member(member)
                .title(contentCreateRequestDto.getTitle())
                .content(contentCreateRequestDto.getContent())
                .createAt(LocalDateTime.now())
                .isImage(contentCreateRequestDto.isImage())
                .isShow(contentCreateRequestDto.isShow())
                .build();

        return ContentResponseDto.of(contentRepository.save(content));
    }

    @Transactional
    public List<ContentResponseDto> findAllContent() {
        List<Content> contents = contentRepository.findAll();
        List<ContentResponseDto> resultList = contents
                .stream()
                .map(list -> modelMapper.map(list, ContentResponseDto.class))
                .collect(Collectors.toList());
        return resultList;
    }

    @Transactional
    public ContentDeleteResponseDto deleteContentByName(Long contentId) {
        contentRepository.deleteById(contentId);
        ContentDeleteResponseDto contentDeleteResponseDto = new ContentDeleteResponseDto();
        contentDeleteResponseDto.setSuccess(true);
        return contentDeleteResponseDto;
    }
}
