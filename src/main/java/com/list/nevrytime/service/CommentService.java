package com.list.nevrytime.service;

import com.list.nevrytime.entity.Comment;
import com.list.nevrytime.entity.Content;
import com.list.nevrytime.entity.Member;
import com.list.nevrytime.exception.CustomException;
import com.list.nevrytime.repository.CommentRepository;
import com.list.nevrytime.repository.ContentRepository;
import com.list.nevrytime.repository.MemberRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
    public CommentResponseDto createComment(Long uid, CommentCreateRequestDto commentCreateRequestDto) {

        Member member = memberRepository.findById(uid).orElseThrow(
                () -> new CustomException(HttpStatus.BAD_REQUEST, "memberId가 유효하지 않습니다."));

        Content content = contentRepository.findById(commentCreateRequestDto.getContentId()).orElseThrow(
                () -> new CustomException(HttpStatus.BAD_REQUEST, "contentId가 유효하지 않습니다."));


        Comment comment = Comment.builder()
                .content(content)
                .member(member)
                .commentContent(commentCreateRequestDto.getCommentContent())
                .parentId(commentCreateRequestDto.getParentId())
                .depth(commentCreateRequestDto.getDepth())
                .createAt(LocalDateTime.now())
                .isDeleted(false)
                .build();

        content.setCommentCount(content.getCommentCount() + 1);
        contentRepository.save(content);

        return CommentResponseDto.of(commentRepository.save(comment));
    }

    @Transactional
    public CommentDeleteResponseDto deleteComment(Long uid,Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CustomException(HttpStatus.BAD_REQUEST, "commentId가 유효하지 않습니다."));

        if (comment.getMember().getId() != uid) {
            throw new CustomException(HttpStatus.BAD_REQUEST, "본인의 댓글만 삭제 할 수 있습니다.");
        }
        if (comment.isDeleted()) {
            throw new CustomException(HttpStatus.BAD_REQUEST, "이미 삭제된 댓글 입니다.");
        }
        Content content = contentRepository.findById(comment.getContent().getId())
                .orElseThrow(() -> new CustomException(HttpStatus.BAD_REQUEST, "contentId가 유효하지 않습니다."));
        content.setCommentCount(content.getCommentCount() - 1);
        contentRepository.save(content);

        comment.setCommentContent("삭제된 댓글 입니다.");
        comment.setDeleted(true);
        commentRepository.save(comment);
        return new CommentDeleteResponseDto(true, commentId);
    }
}
