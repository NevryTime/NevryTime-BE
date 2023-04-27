package com.list.nevrytime.service;

import com.list.nevrytime.entity.Comment;
import com.list.nevrytime.entity.Content;
import com.list.nevrytime.entity.Member;
import com.list.nevrytime.entity.QComment;
import com.list.nevrytime.repository.CommentRepository;
import com.list.nevrytime.repository.ContentRepository;
import com.list.nevrytime.repository.MemberRepository;
import com.list.nevrytime.util.SecurityUtil;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static com.list.nevrytime.dto.CommentDto.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService {

    private final JPAQueryFactory jpaQueryFactory;
    private final MemberRepository memberRepository;
    private final ContentRepository contentRepository;
    private final CommentRepository commentRepository;

    @Transactional
    public CommentResponseDto createComment(CommentCreateRequestDto commentCreateRequestDto) {

        Member member = memberRepository.findById(SecurityUtil.getCurrentMemberId()).orElseThrow(
                () -> new RuntimeException("memberId가 유효하지 않습니다."));

        Content content = contentRepository.findById(commentCreateRequestDto.getContentId()).orElseThrow(
                () -> new RuntimeException("contentId가 유효하지 않습니다."));


        Comment comment = Comment.builder()
                .content(content)
                .member(member)
                .commentContent(commentCreateRequestDto.getCommentContent())
                .parentId(commentCreateRequestDto.getParentId())
                .depth(commentCreateRequestDto.getDepth())
                .createAt(LocalDateTime.now())
                .build();

        content.setCommentCount(content.getCommentCount() + 1);
        contentRepository.save(content);

        return CommentResponseDto.of(commentRepository.save(comment));
    }

    @Transactional
    public List<CommentResponseDto> findCommentByContentId(Long contentId) {
        QComment qComment = new QComment("comment");
        return jpaQueryFactory
                .select(Projections.constructor(
                        CommentResponseDto.class,
                        qComment.id.as("id"),
                                qComment.content.id.as("contentId"),
                                qComment.member.id.as("memberId"),
                                qComment.commentContent.as("commentContent"),
                                qComment.parentId.as("parentId"),
                                qComment.depth.as("layerLevel"),
                                qComment.createAt.as("createAt")
                ))
                .from(qComment)
                .innerJoin(qComment.content)
                .where(qComment.content.id.eq(contentId))
                .orderBy(qComment.id.desc())
                .fetch();
    }
}
