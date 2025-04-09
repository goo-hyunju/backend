package com.tradelog.backend.controller;

import com.tradelog.backend.dto.journal.JournalResponse;
import com.tradelog.backend.service.AiFeedbackService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "AI 피드백 API", description = "매매일지 AI 피드백 관련 API")
public class AiFeedbackController {

    private final AiFeedbackService aiFeedbackService;

    @GetMapping("/journals/{journalId}/ai-feedback")
    @Operation(
            summary = "AI 피드백 생성",
            description = "특정 매매일지에 대한 AI 피드백을 생성합니다.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseEntity<JournalResponse> generateAiFeedback(@PathVariable Long journalId) {
        return ResponseEntity.ok(aiFeedbackService.generateAiFeedback(journalId));
    }
} 