package com.gsm.blabla.content.dao;

import com.gsm.blabla.content.domain.Content;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

@SpringBootTest
class ContentRepositoryTest {

    @AfterEach
    void afterEach() {
        contentRepository.deleteAll();
    }

    @Autowired
    private ContentRepository contentRepository;

    @DisplayName("영어로 컨텐츠 리스트를 조회한다.")
    @Test
    void findAllByLanguage() {
        // given
        Content content1 = createContent("인턴", "영화 인턴을 통해 비즈니스 표현을 배워봅시다.", "en");
        Content content2 = createContent("셜록", "셜록을 통해 영국 억양과 일상 표현을 배워봅시다.", "en");
        Content content3 = createContent("오징어 게임", "드라마 오징어 게임을 통해 줄임말 표현을 배워봅시다.", "ko");
        Content content4 = createContent("왕좌의 게임", "왕좌의 게임을 통해 전쟁 표현을 배워봅시다.", "en");
        contentRepository.saveAll(List.of(content1, content2, content3, content4));

        // when
        List<Content> contents = contentRepository.findAllByLanguage("en");

        // then
        assertThat(contents).hasSize(3)
                .extracting("title", "description", "language")
                .containsExactlyInAnyOrder(
                        tuple("인턴", "영화 인턴을 통해 비즈니스 표현을 배워봅시다.", "en"),
                        tuple("셜록", "셜록을 통해 영국 억양과 일상 표현을 배워봅시다.", "en"),
                        tuple("왕좌의 게임", "왕좌의 게임을 통해 전쟁 표현을 배워봅시다.", "en")
                );
    }

    private static Content createContent(String title, String description, String language) {
        return Content.builder()
                .title(title)
                .description(description)
                .language(language)
                .thumbnailURL("https://img.youtube.com/vi/sHpGT4SQwgw/hqdefault.jpg")
                .build();
    }

}
