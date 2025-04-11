package com.tradelog.backend.controller;

import com.tradelog.backend.dto.JournalDto;
import com.tradelog.backend.entity.Journal;
import com.tradelog.backend.service.JournalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/journals")
@RequiredArgsConstructor
@Tag(name = "매매일지 API", description = "매매일지 관련 API")
public class JournalController {
    private final JournalService journalService;

    @GetMapping
    @Operation(summary = "공개 매매일지 목록 조회", description = "공개된 모든 매매일지를 조회합니다.")
    public ResponseEntity<List<Journal>> getAllPublicJournals() {
        return ResponseEntity.ok(journalService.findAllPublicJournals());
    }

    @GetMapping("/me")
    @Operation(summary = "내 매매일지 목록 조회", description = "로그인한 사용자의 매매일지를 조회합니다.")
    public ResponseEntity<List<Journal>> getMyJournals(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(journalService.findJournalsByUser(userDetails.getUsername()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "매매일지 상세 조회", description = "특정 매매일지의 상세 정보를 조회합니다.")
    public ResponseEntity<Journal> getJournalById(@PathVariable Long id) {
        return ResponseEntity.ok(journalService.findById(id));
    }

    @PostMapping
    @Operation(summary = "매매일지 작성", description = "새로운 매매일지를 작성합니다.")
    public ResponseEntity<Journal> createJournal(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody JournalDto.CreateRequest request) {
        return ResponseEntity.ok(journalService.createJournal(userDetails.getUsername(), request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "매매일지 수정", description = "특정 매매일지를 수정합니다.")
    public ResponseEntity<Journal> updateJournal(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody JournalDto.UpdateRequest request) {
        return ResponseEntity.ok(journalService.updateJournal(id, userDetails.getUsername(), request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "매매일지 삭제", description = "특정 매매일지를 삭제합니다.")
    public ResponseEntity<Void> deleteJournal(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        journalService.deleteJournal(id, userDetails.getUsername());
        return ResponseEntity.noContent().build();
    }
} 