package com.list.nevrytime.repository;

import com.list.nevrytime.entity.Content;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ContentRepository extends JpaRepository<Content, Long> {

    Optional<Content> findContentByMember_IdAndId(Long memberId, Long contentId);
    Page<Content> findByBoardId(Long boardId, Pageable pageable);
    List<Content> findTop2ByOrderByLikesDescContentDesc();
}
