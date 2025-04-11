package com.tradelog.backend.controller;

import com.tradelog.backend.dto.AiFeedbackDto;
import com.tradelog.backend.entity.AiFeedback;
import com.tradelog.backend.service.AiFeedbackService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/journals/{journalId}/ai-feedback")
@RequiredArgsConstructor
@Tag(name = "AI 피드백 API", description = "매매일지 AI 피드백 관련 API")
public class AiFeedbackController {
    private final AiFeedbackService aiFeedbackService;

    @GetMapping
    @Operation(summary = "AI 피드백 조회", description = "특정 매매일지의 AI 피드백을 조회합니다.")
    public ResponseEntity<AiFeedback> getAiFeedback(@PathVariable Long journalId) {
        Optional<AiFeedback> aiFeedback = aiFeedbackService.findByJournal(journalId);
        return aiFeedback.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "AI 피드백 생성", description = "AI를 통해 매매일지 피드백을 자동 생성합니다.")
    public ResponseEntity<AiFeedback> generateAiFeedback(
            @PathVariable Long journalId,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(aiFeedbackService.generateAiFeedback(journalId, userDetails.getUsername()));
    }

    @PostMapping("/manual")
    @Operation(summary = "AI 피드백 수동 생성/수정", description = "매매일지 피드백을 수동으로 생성하거나 수정합니다.")
    public ResponseEntity<AiFeedback> createOrUpdateAiFeedback(
            @PathVariable Long journalId,
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody AiFeedbackDto.CreateRequest request) {
        return ResponseEntity.ok(aiFeedbackService.createOrUpdateAiFeedback(journalId, userDetails.getUsername(), request));
    }
}  