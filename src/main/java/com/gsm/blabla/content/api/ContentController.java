package com.gsm.blabla.content.api;

import com.gsm.blabla.content.application.ContentService;
import com.gsm.blabla.content.dto.*;
import com.gsm.blabla.global.response.DataResponseDto;
import com.gsm.blabla.content.dto.PracticeFeedbackResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@Validated
@Tag(name = "연습실 관련 API")
@RestController
@RequiredArgsConstructor
public class ContentController {

    private final ContentService practiceService;

    @Operation(summary = "컨텐츠 전체 조회 API")
    @GetMapping("/contents")
    public DataResponseDto<Map<String, List<ContentsResponseDto>>> getContents(
            @Pattern(regexp = "^(ko|en)$", message = "언어는 ko 또는 en 중 하나여야 합니다.")
            @RequestHeader(name="Content-Language") String language) {
        return DataResponseDto.of(practiceService.getContents(language));
    }

    @Operation(summary = "세부 컨텐츠 전체 조회 API")
    @GetMapping("/contents/{contentId}")
    public DataResponseDto<ContentDetailsResponseDto> getContentDetails(
            @Positive(message = "contentId는 양수여야 합니다.") @PathVariable Long contentId) {
        return DataResponseDto.of(practiceService.getContentDetails(contentId));
    }

    @Operation(summary = "세부 컨텐츠 단일 조회 API")
    @GetMapping("/contents/detail/{contentDetailId}")
    public DataResponseDto<ContentDetailResponseDto> getContentDetail(
            @Positive(message = "contentDetailId는 양수여야 합니다.") @PathVariable Long contentDetailId) {
        return DataResponseDto.of(practiceService.getContentDetail(contentDetailId));
    }

    @Operation(summary = "연습실 피드백 생성 & 조회 API")
    @PostMapping("/contents/detail/{contentDetailId}/feedback")
    public DataResponseDto<PracticeFeedbackResponseDto> createFeedback(
            @PathVariable Long contentDetailId,
            @RequestBody UserAnswerRequestDto userAnswerRequestDto) {
        return DataResponseDto.of(practiceService.createFeedback(contentDetailId, userAnswerRequestDto));
    }

    @Operation(summary = "연습실 피드백 조회 API")
    @GetMapping("/contents/detail/{contentDetailId}/feedback")
    public DataResponseDto<PracticeFeedbackResponseDto> getFeedback(
            @PathVariable Long contentDetailId) {
        return DataResponseDto.of(practiceService.getFeedback(contentDetailId));
    }

    @Operation(summary = "연습 기록 음성 파일 저장 API")
    @PostMapping("/contents/detail/{contentDetailId}/practice")
    public DataResponseDto<Map<String, String>> createPracticeHistory(
            @PathVariable Long contentDetailId,
            @RequestParam("files") List<MultipartFile> files) {
        return DataResponseDto.of(practiceService.savePracticeHistory(contentDetailId, files));
    }
}


