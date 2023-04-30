package com.list.nevrytime.service;

import com.list.nevrytime.dto.ContentDto.ContentCreateRequestDto;
import com.list.nevrytime.entity.*;
import com.list.nevrytime.exception.CustomException;
import com.list.nevrytime.repository.BoardRepository;
import com.list.nevrytime.repository.CommentRepository;
import com.list.nevrytime.repository.ContentRepository;
import com.list.nevrytime.repository.MemberRepository;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
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
    public ContentResponseDto createContent(Long uid, ContentCreateRequestDto contentCreateRequestDto) {
        Board board = boardRepository.findById(contentCreateRequestDto.getBoardId()).orElseThrow(
                () -> new CustomException(HttpStatus.BAD_REQUEST, "boardId가 유효하지 않습니다."));

        Member member = memberRepository.findById(uid).orElseThrow(
                () -> new CustomException(HttpStatus.BAD_REQUEST, "로그인 인증이 정상적으로 처리되지 않은 상태입니다."));

        Content content = Content.builder()
                .board(board)
                .member(member)
                .title(contentCreateRequestDto.getTitle())
                .content(contentCreateRequestDto.getContent())
                .createAt(LocalDateTime.now())
                .isImage(contentCreateRequestDto.isImage())
                .isShow(contentCreateRequestDto.isShow())
                .commentCount(0)
                .scraps(0)
                .likes(0)
                .build();

        return ContentResponseDto.of(contentRepository.save(content));
    }

    @Transactional
    public ContentWithCommentResponseDto findContentById(Long contentId) {
        ContentResponseDto contentResponseDto = ContentResponseDto.of(contentRepository.findById(contentId)
                .orElseThrow(() -> new CustomException(HttpStatus.BAD_REQUEST, "게시판이 존재하지 않습니다.")));

        QComment qComment = new QComment("comment");

        List<CommentResponseDto> commentResponseDtoList = jpaQueryFactory
                .select(Projections.constructor(
                        CommentResponseDto.class,
                        qComment.id.as("id"),
                        qComment.content.id.as("contentId"),
                        qComment.member.nickName.as("nickName"),
                        qComment.commentContent.as("commentContent"),
                        qComment.parentId.as("parentId"),
                        qComment.depth.as("depth"),
                        qComment.isDeleted.as("isDeleted"),
                        qComment.createAt.as("createAt")
                ))
                .from(qComment)
                .innerJoin(qComment.content)
                .where(qComment.content.id.eq(contentId))
                .fetch();

        return new ContentWithCommentResponseDto(contentResponseDto, commentResponseDtoList);
    }

    @Transactional
    public List<ContentResponseDto> findContents(Long uid) {
        QContent qContent = new QContent("content");
        try {
            List<ContentResponseDto> contentResponseDtoList = jpaQueryFactory
                    .select(Projections.constructor(
                            ContentResponseDto.class,
                            qContent.id, qContent.board.name, qContent.member.name,
                            qContent.title, qContent.content, qContent.commentCount,
                            qContent.scraps, qContent.likes, qContent.createAt,
                            qContent.isImage, qContent.isShow))
                    .from(qContent)
                    .innerJoin(qContent.member)
                    .where(qContent.member.id.eq(uid))
                    .fetch();

            return contentResponseDtoList;
        } catch (Exception e) {
            throw new CustomException(HttpStatus.BAD_REQUEST, "작성한 글이 없습니다.");
        }
    }

    @Transactional
    public List<ContentResponseDto> findMyCommentInContent(Long uid) {
        QContent qContent = new QContent("content");
        QComment qComment = new QComment("comment");
        try {
            List<ContentResponseDto> contentResponseDtoList = jpaQueryFactory
                    .select(Projections.constructor(
                            ContentResponseDto.class,
                            qContent.id, qContent.board.name, qContent.member.nickName,
                            qContent.title, qContent.content, qContent.commentCount,
                            qContent.scraps, qContent.likes, qContent.createAt,
                            qContent.isImage, qContent.isShow))
                    .from(qContent)
                    .where(qContent.id.in(JPAExpressions
                            .select(qComment.content.id)
                            .from(qComment)
                            .where(qComment.member.id.eq(uid))
                    ))
                    .fetch();

            return contentResponseDtoList;
        } catch (Exception e) {
            throw new CustomException(HttpStatus.BAD_REQUEST, "댓글을 작성한 글이 없습니다.");
        }
    }

    @Transactional
    public List<ContentResponseDto> findMyScrap(Long uid) {
        QContent qContent = new QContent("content");
        QScrap qScrap = new QScrap("scrap");
        List<ContentResponseDto> contentResponseDtoList = jpaQueryFactory
                .select(Projections.constructor(
                        ContentResponseDto.class,
                        qContent.id, qContent.board.name, qContent.member.nickName,
                        qContent.title, qContent.content, qContent.commentCount,
                        qContent.scraps, qContent.likes, qContent.createAt,
                        qContent.isImage, qContent.isShow))
                .from(qContent)
                .where(qContent.id.in(JPAExpressions
                        .select(qScrap.content.id)
                        .from(qScrap)
                        .where(qScrap.member.id.eq(uid))
                ))
                .fetch();
        return contentResponseDtoList;
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
    public ContentDeleteResponseDto deleteContentByName(Long uid, Long contentId) {
        Content content = contentRepository.findContentByMember_IdAndId(uid, contentId).orElseThrow(
                () -> new CustomException(HttpStatus.BAD_REQUEST, "게시판이 존재하지 않습니다."));
        contentRepository.deleteById(contentId);
        return new ContentDeleteResponseDto(true);
    }

    @Transactional
    public ContentUpdateResponseDto update(Long contentId, ContentUpdateRequestDto contentUpdateRequestDto) {
        Content findContent = contentRepository.findById(contentId)
                .orElseThrow(() -> new CustomException(HttpStatus.BAD_REQUEST, "게시글이 존재하지 않습니다."));

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
                .commentCount(findContent.getCommentCount())
                .scraps(findContent.getScraps())
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
                content -> new ContentResponseDto(content.getId(), content.getBoard().getName(), content.getMember().getName(), content.getTitle(), content.getContent(), content.getCommentCount(), content.getScraps(), content.getLikes(), content.getCreateAt(), content.isImage(), content.isShow()));
        return new ContentPageResponseDto(true, toMap.getContent(), toMap.getTotalPages(), toMap.getTotalElements());
    }
}
