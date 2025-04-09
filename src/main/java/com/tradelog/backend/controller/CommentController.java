package com.tradelog.backend.controller;

import com.tradelog.backend.dto.comment.CommentRequest;
import com.tradelog.backend.dto.comment.CommentResponse;
import com.tradelog.backend.service.CommentService;
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
@Tag(name = "댓글 API", description = "매매일지 댓글 관련 API")
public class CommentController {

    private final CommentService commentService;

    @GetMapping("/journals/{journalId}/comments")
    @Operation(summary = "댓글 조회", description = "특정 매매일지의 댓글을 조회합니다.")
    public ResponseEntity<Page<CommentResponse>> getJournalComments(
            @PathVariable Long journalId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return ResponseEntity.ok(commentService.getJournalComments(journalId, pageable));
    }

    @PostMapping("/journals/{journalId}/comments")
    @Operation(
            summary = "댓글 작성",
            description = "특정 매매일지에 댓글을 작성합니다.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseEntity<CommentResponse> createComment(
            @PathVariable Long journalId,
            @Valid @RequestBody CommentRequest request
    ) {
        return ResponseEntity.ok(commentService.createComment(journalId, request));
    }

    @DeleteMapping("/comments/{commentId}")
    @Operation(
            summary = "댓글 삭제",
            description = "특정 댓글을 삭제합니다.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseEntity<Void> deleteComment(@PathVariable Long commentId) {
        commentService.deleteComment(commentId);
        return ResponseEntity.ok().build();
    }
} 