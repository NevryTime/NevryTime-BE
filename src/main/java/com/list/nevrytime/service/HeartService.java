package com.list.nevrytime.service;

import com.list.nevrytime.dto.BoardDto;
import com.list.nevrytime.entity.Content;
import com.list.nevrytime.entity.Heart;
import com.list.nevrytime.entity.Member;
import com.list.nevrytime.repository.ContentRepository;
import com.list.nevrytime.repository.HeartRepository;
import com.list.nevrytime.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.list.nevrytime.dto.HeartDto.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class HeartService {

    private final HeartRepository heartRepository;
    private final MemberRepository memberRepository;
    private final ContentRepository contentRepository;

    @Transactional
    public HeartResponseDto insert(HeartRequestDto heartRequestDto) {

        Member member = memberRepository.findById(heartRequestDto.getMemberId())
                .orElseThrow(() -> new RuntimeException("유저가 존재하지 않습니다."));

        Content content = contentRepository.findById(heartRequestDto.getContentId())
                .orElseThrow(() -> new RuntimeException("게시글이 존재하지 않습니다."));

        // 이미 좋아요되어있으면 에러 반환
        if (heartRepository.findByMemberIdAndContentId(member.getId(), content.getId()).isPresent()){
            throw new RuntimeException("이미 좋아요가 되어있습니다.");
        }
        content.setLikes(content.getLikes() + 1);
        contentRepository.save(content);


        Heart heart = Heart.builder()
                .member(member)
                .content(content)
                .build();

        return HeartResponseDto.of(heartRepository.save(heart));
    }

    @Transactional
    public HeartDeleteResponseDto delete(HeartRequestDto heartRequestDto) {

        Member member = memberRepository.findById(heartRequestDto.getMemberId())
                .orElseThrow(() -> new RuntimeException("유저가 존재하지 않습니다."));

        Content content = contentRepository.findById(heartRequestDto.getContentId())
                .orElseThrow(() -> new RuntimeException("게시글이 존재하지 않습니다."));

        Heart heart = heartRepository.findByMemberIdAndContentId(member.getId(), content.getId())
                .orElseThrow(() -> new RuntimeException("좋아요 기록이 존재하지 않습니다."));

        content.setLikes(content.getLikes() - 1);
        contentRepository.save(content);

        heartRepository.delete(heart);
        return new HeartDeleteResponseDto(true);
    }
}
