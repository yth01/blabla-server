package com.gsm.blabla.content.application;

import com.gsm.blabla.content.dao.ContentRepository;
import com.gsm.blabla.content.dao.MemberContentRepository;
import com.gsm.blabla.content.domain.Content;
import com.gsm.blabla.content.dto.ContentListResponseDto;
import com.gsm.blabla.content.dto.ContentResponseDto;
import com.gsm.blabla.content.dto.ContentViewResponseDto;
import com.gsm.blabla.global.exception.GeneralException;
import com.gsm.blabla.global.response.Code;
import com.gsm.blabla.global.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ContentService {
    private final ContentRepository contentRepository;
    private final MemberContentRepository memberContentRepository;

    @Transactional(readOnly = true)
    public ContentResponseDto get(Long contentId) {
        Content content = contentRepository.findById(contentId).orElseThrow(
                () -> new GeneralException(Code.CONTENT_NOT_FOUND, "존재하지 않는 컨텐츠입니다.")
        );
        return ContentResponseDto.contentResponse(content);
    }

    @Transactional(readOnly = true)
    public Map<String, ContentListResponseDto> getAll(String language) {
        Map<String, ContentListResponseDto> result = new HashMap<>();

        List<Content> allContents = switch (language) {
            case "ko" -> contentRepository.findAllByLanguage("ko");
            case "en" -> contentRepository.findAllByLanguage("en");
            default -> new ArrayList<>();
        };

        final Long memberId = SecurityUtil.getMemberId();

        Map<Long, List<Content>> contentsByLevel = allContents.stream()
                .collect(Collectors.groupingBy(Content::getLevel));
        for (Map.Entry<Long, List<Content>> entry : contentsByLevel.entrySet()) {
            Long level = entry.getKey();
            List<Content> contentsForLevel = entry.getValue();
            List<ContentViewResponseDto> contentResponseForLevel = ContentViewResponseDto.contentViewListResponse(contentsForLevel, memberId, memberContentRepository);

            result.put("level" + level, ContentListResponseDto.contentListResponse(contentResponseForLevel));
        }

        return result;
    }

    @Transactional(readOnly = true)
    public ContentViewResponseDto getTodayContent(String language) {

        List<Content> contents = switch (language) {
            case "ko" -> contentRepository.findAllByLanguage("ko");
            case "en" -> contentRepository.findAllByLanguage("en");
            default -> new ArrayList<>();
        };

        final Long memberId = SecurityUtil.getMemberId();

        Optional<Content> todayContents = contents.stream()
                .filter(content -> memberContentRepository.findByContentIdAndMemberId(content.getId(), memberId).isEmpty())
                .findFirst();

        return todayContents.map(content -> ContentViewResponseDto.contentViewResponse(content, memberId, memberContentRepository))
                .orElse(null);
    }
}
