package com.list.nevrytime.service;

import com.list.nevrytime.entity.Comment;
import com.list.nevrytime.entity.Content;
import com.list.nevrytime.entity.Member;
import com.list.nevrytime.entity.QComment;
import com.list.nevrytime.exception.CustomException;
import com.list.nevrytime.repository.CommentRepository;
import com.list.nevrytime.repository.ContentRepository;
import com.list.nevrytime.repository.MemberRepository;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
                .isShow(commentCreateRequestDto.isShow())
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

    @Transactional
    public Comment updateComment(Long uid, UpdateCommentRequestDto updateCommentRequestDto) {
        Comment comment = commentRepository.findById(uid)
                .orElseThrow(() -> new CustomException(HttpStatus.BAD_REQUEST, "commentId가 유효하지 않습니다."));

        if (comment.isDeleted()) {
            throw new CustomException(HttpStatus.BAD_REQUEST, "이미 삭제된 댓글 입니다.");
        }

        Comment newComment = Comment.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .member(comment.getMember())
                .commentContent(updateCommentRequestDto.getCommentContent())
                .parentId(comment.getParentId())
                .depth(comment.getDepth())
                .createAt(LocalDateTime.now())
                .isDeleted(false)
                .isShow(updateCommentRequestDto.isShow())
                .build();
        return commentRepository.save(newComment);
    }

    public List<CommentResponseDto> findCommentInContent(Long contentId) {
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
                        qComment.createAt.as("createAt"),
                        qComment.isShow.as("isShow")
                ))
                .from(qComment)
                .innerJoin(qComment.content)
                .where(qComment.content.id.eq(contentId))
                .fetch();

        for (CommentResponseDto commentResponseDto : commentResponseDtoList) {
            if (!commentResponseDto.isShow()) {
                commentResponseDto.setNickName("익명");
            }
            Comment comment = commentRepository.findById(commentResponseDto.getId())
                    .orElseThrow(() -> new CustomException(HttpStatus.BAD_REQUEST, "commentId가 유효하지 않습니다."));
            if (contentId == comment.getMember().getId()) {
                commentResponseDto.setNickName(commentResponseDto.getNickName() + "(글쓴이)");
            }
        }
        return commentResponseDtoList;
    }
}
