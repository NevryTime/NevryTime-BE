package com.list.nevrytime.service;

import com.list.nevrytime.entity.Comment;
import com.list.nevrytime.entity.Content;
import com.list.nevrytime.entity.Member;
import com.list.nevrytime.repository.CommentRepository;
import com.list.nevrytime.repository.ContentRepository;
import com.list.nevrytime.repository.MemberRepository;
import com.list.nevrytime.security.util.SecurityUtil;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

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
    public CommentDeleteResponseDto deleteComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("commentId가 유효하지 않습니다."));
        if (comment.isDeleted()) {
            throw new RuntimeException("이미 삭제된 댓글 입니다.");
        }
        Content content = contentRepository.findById(comment.getContent().getId())
                .orElseThrow(() -> new RuntimeException("contentId가 유효하지 않습니다."));
        content.setCommentCount(content.getCommentCount() - 1);
        contentRepository.save(content);
        comment.setCommentContent("삭제된 댓글 입니다.");
        commentRepository.save(comment);
        return new CommentDeleteResponseDto(true, commentId);
    }
}
