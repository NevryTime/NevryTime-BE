package com.list.nevrytime.service;

import com.list.nevrytime.dto.CommentDto;
import com.list.nevrytime.dto.ContentDto;
import com.list.nevrytime.dto.ContentDto.ContentCreateRequestDto;
import com.list.nevrytime.entity.*;
import com.list.nevrytime.jwt.TokenProvider;
import com.list.nevrytime.repository.BoardRepository;
import com.list.nevrytime.repository.CommentRepository;
import com.list.nevrytime.repository.ContentRepository;
import com.list.nevrytime.repository.MemberRepository;
import com.list.nevrytime.util.SecurityUtil;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.list.nevrytime.dto.CommentDto.*;
import static com.list.nevrytime.dto.ContentDto.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ContentService {

    private final JPAQueryFactory jpaQueryFactory;
    private final ModelMapper modelMapper;
    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;
    private final ContentRepository contentRepository;
    private final CommentRepository commentRepository;

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
    public ContentWithCommentResponseDto findContentById(Long contentId) {
        ContentResponseDto contentResponseDto = ContentResponseDto.of(contentRepository.findById(contentId)
                .orElseThrow(() -> new RuntimeException("게시판이 존재하지 않습니다.")));

        QComment qComment = new QComment("comment");

        List<CommentResponseDto> commentResponseDtos = jpaQueryFactory
                .select(Projections.constructor(
                        CommentResponseDto.class,
                        qComment.id.as("id"),
                        qComment.content.id.as("contentId"),
                        qComment.member.id.as("memberId"),
                        qComment.commentContent.as("commentContent"),
                        qComment.parentId.as("parentId"),
                        qComment.depth.as("depth"),
                        qComment.createAt.as("createAt")
                ))
                .from(qComment)
                .innerJoin(qComment.content)
                .where(qComment.content.id.eq(contentId))
                .orderBy(qComment.id.desc())
                .fetch();

//        List<Comment> comments = commentRepository.findCommentsByContentId(contentId);
//        List<CommentResponseDto> resultList = comments
//                .stream()
//                .map(list -> modelMapper.map(list, CommentResponseDto.class))
//                .collect(Collectors.toList());

        return new ContentWithCommentResponseDto(contentResponseDto,commentResponseDtos);
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
        Content content = contentRepository.findContentByMember_IdAndId(SecurityUtil.getCurrentMemberId(), contentId).orElseThrow(
                () -> new RuntimeException("게시판이 존재하지 않습니다."));
        contentRepository.deleteById(contentId);
        return new ContentDeleteResponseDto(true);
    }

    @Transactional
    public ContentUpdateResponseDto update(Long contentId, ContentUpdateRequestDto contentUpdateRequestDto) {
        Content findContent = contentRepository.findById(contentId)
                .orElseThrow(() -> new RuntimeException("게시글이 존재하지 않습니다."));

        Content content = Content.builder()
                .id(findContent.getId())
                .board(findContent.getBoard())
                .member(findContent.getMember())
                .title(contentUpdateRequestDto.getTitle())
                .content(contentUpdateRequestDto.getContent())
                .isImage(contentUpdateRequestDto.getIsImage())
                .isShow(contentUpdateRequestDto.getIsShow())
                .likes(findContent.getLikes())
                .createAt(LocalDateTime.now())
                .build();

        return new ContentUpdateResponseDto(true, ContentResponseDto.of(contentRepository.save(content)));
    }

    @Transactional
    public ContentPageResponseDto pageContent(Long boardId, int page, int length) {
        PageRequest pageRequest = PageRequest.of(page, length, Sort.Direction.DESC, "CreateAt");
        Page<Content> pageContent = contentRepository.findByBoardId(boardId, pageRequest);
        return getContentPageResponseDto(pageContent);
    }

    @Transactional
    public List<ContentResponseDto> popularContent() {
        List<Content> contents = contentRepository.findTop2ByOrderByLikesDescContentDesc();
        List<ContentResponseDto> resultList = contents
                .stream()
                .map(list -> modelMapper.map(list, ContentResponseDto.class))
                .collect(Collectors.toList());
        return resultList;
    }

    @Transactional
    public ContentPageResponseDto hotContent(int page) {

        PageRequest pageRequest = PageRequest.of(page, 20, Sort.Direction.DESC, "CreateAt");
        Page<Content> contents = contentRepository.findByLikesGreaterThanOrderByCreateAtDesc(9, pageRequest);
        return getContentPageResponseDto(contents);
    }

    @Transactional
    public List<ContentResponseDto> hotListContent() {
        List<Content> contents = contentRepository.findTop4ByLikesGreaterThanOrderByCreateAtDesc(9);
        List<ContentResponseDto> resultList = contents
                .stream()
                .map(list -> modelMapper.map(list, ContentResponseDto.class))
                .collect(Collectors.toList());
        return resultList;
    }

    @Transactional
    public ContentPageResponseDto bestContent(int page) {
        PageRequest pageRequest = PageRequest.of(page, 20, Sort.Direction.DESC, "CreateAt");
        Page<Content> contents = contentRepository.findByLikesGreaterThanOrderByCreateAtDesc(99, pageRequest);
        return getContentPageResponseDto(contents);
    }

    private ContentPageResponseDto getContentPageResponseDto(Page<Content> contents) {
        Page<ContentResponseDto> toMap = contents.map(
                content -> new ContentResponseDto(content.getId(), content.getBoard().getName(), content.getMember().getName(), content.getTitle(), content.getContent(), content.getCommentCount(), content.getHearts(), content.getLikes(), content.getCreateAt(), content.isImage(), content.isShow()));
        return new ContentPageResponseDto(true, toMap.getContent(), toMap.getTotalPages(), toMap.getTotalElements());
    }
}
