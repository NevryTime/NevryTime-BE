package com.list.nevrytime.repository;

import com.list.nevrytime.entity.Content;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ContentRepository extends JpaRepository<Content, Long> {

    Optional<Content> findContentAllByMember_Id(Long Id);
    Optional<Content> findContentByMember_IdAndId(Long memberId, Long contentId);
}
