package com.list.nevrytime.service;

import com.list.nevrytime.entity.Content;
import com.list.nevrytime.entity.Member;
import com.list.nevrytime.entity.Scrap;
import com.list.nevrytime.exception.CustomException;
import com.list.nevrytime.repository.ContentRepository;
import com.list.nevrytime.repository.MemberRepository;
import com.list.nevrytime.repository.ScrapRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.list.nevrytime.dto.ScrapDto.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ScrapService {

    private final ScrapRepository scrapRepository;
    private final MemberRepository memberRepository;
    private final ContentRepository contentRepository;

    @Transactional
    public ScrapResponseDto insert(Long uid, ScrapRequestDto scrapRequestDto) {

        Member member = memberRepository.findById(uid)
                .orElseThrow(() -> new CustomException(HttpStatus.BAD_REQUEST, "로그인 인증이 정상적으로 처리되지 않은 상태입니다."));

        Content content = contentRepository.findById(scrapRequestDto.getContentId())
                .orElseThrow(() -> new CustomException(HttpStatus.BAD_REQUEST, "게시글이 존재하지 않습니다."));

        // 이미 스크랩되어있으면 에러 반환
        if (scrapRepository.findByMemberIdAndContentId(member.getId(), content.getId()).isPresent()) {
            throw new CustomException(HttpStatus.BAD_REQUEST, "이미 스크랩이 되어있습니다.");
        }


        content.setScraps(content.getScraps() + 1);
        contentRepository.save(content);

        Scrap scrap = Scrap.builder()
                .member(member)
                .content(content)
                .build();

        return ScrapResponseDto.of(scrapRepository.save(scrap));
    }

    @Transactional
    public ScrapDeleteResponseDto delete(Long uid, ScrapRequestDto scrapRequestDto) {

        Member member = memberRepository.findById(uid)
                .orElseThrow(() -> new CustomException(HttpStatus.BAD_REQUEST, "로그인 인증이 정상적으로 처리되지 않은 상태입니다."));

        Content content = contentRepository.findById(scrapRequestDto.getContentId())
                .orElseThrow(() -> new CustomException(HttpStatus.BAD_REQUEST, "게시글이 존재하지 않습니다."));

        Scrap scrap = scrapRepository.findByMemberIdAndContentId(member.getId(), content.getId())
                .orElseThrow(() -> new CustomException(HttpStatus.BAD_REQUEST, "좋아요 기록이 존재하지 않습니다."));

        content.setScraps(content.getScraps() - 1);
        contentRepository.save(content);

        scrapRepository.delete(scrap);
        return new ScrapDeleteResponseDto(true);
    }
}