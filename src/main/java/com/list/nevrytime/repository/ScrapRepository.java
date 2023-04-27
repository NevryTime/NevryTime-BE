package com.list.nevrytime.repository;

import com.list.nevrytime.entity.Scrap;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ScrapRepository extends JpaRepository<Scrap, Long> {
    Optional<Scrap> findByMemberIdAndContentId(Long memberId, Long contentId);
}
