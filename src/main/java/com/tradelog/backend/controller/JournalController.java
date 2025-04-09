package com.tradelog.backend.controller;

import com.tradelog.backend.dto.journal.JournalRequest;
import com.tradelog.backend.dto.journal.JournalResponse;
import com.tradelog.backend.service.JournalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "매매일지 API", description = "매매일지 CRUD 기능 API")
public class JournalController {

    private final JournalService journalService;

    @GetMapping("/journals")
    @Operation(summary = "공개 매매일지 전체 조회", description = "공개된 모든 매매일지를 조회합니다.")
    public ResponseEntity<Page<JournalResponse>> getAllPublicJournals(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return ResponseEntity.ok(journalService.getAllPublicJournals(pageable));
    }

    @GetMapping("/journals/{id}")
    @Operation(summary = "매매일지 상세 조회", description = "특정 매매일지를 ID로 조회합니다.")
    public ResponseEntity<JournalResponse> getJournalById(@PathVariable Long id) {
        return ResponseEntity.ok(journalService.getJournalById(id));
    }

    @GetMapping("/me/journals")
    @Operation(
            summary = "내 매매일지 조회",
            description = "로그인한 사용자의 매매일지를 조회합니다.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseEntity<Page<JournalResponse>> getMyJournals(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return ResponseEntity.ok(journalService.getMyJournals(pageable));
    }

    @PostMapping("/journals")
    @Operation(
            summary = "매매일지 작성",
            description = "새로운 매매일지를 작성합니다.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseEntity<JournalResponse> createJournal(@Valid @RequestBody JournalRequest request) {
        return ResponseEntity.ok(journalService.createJournal(request));
    }

    @PutMapping("/journals/{id}")
    @Operation(
            summary = "매매일지 수정",
            description = "특정 매매일지를 수정합니다.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseEntity<JournalResponse> updateJournal(
            @PathVariable Long id,
            @Valid @RequestBody JournalRequest request
    ) {
        return ResponseEntity.ok(journalService.updateJournal(id, request));
    }

    @DeleteMapping("/journals/{id}")
    @Operation(
            summary = "매매일지 삭제",
            description = "특정 매매일지를 삭제합니다.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseEntity<Void> deleteJournal(@PathVariable Long id) {
        journalService.deleteJournal(id);
        return ResponseEntity.ok().build();
    }
} 