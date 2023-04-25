package com.list.nevrytime.repository;

import com.list.nevrytime.entity.Heart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface HeartRepository extends JpaRepository<Heart, Long> {
    Optional<Heart> findByMemberIdAndContentId(Long memberId, Long contentId);
}
