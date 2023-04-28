package com.list.nevrytime.repository;

import com.list.nevrytime.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByName(String name);
    void deleteMemberById(Long id);
    boolean existsByName(String name);
    boolean existsByNickName(String nickName);
}
