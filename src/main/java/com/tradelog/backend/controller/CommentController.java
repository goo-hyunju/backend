package com.tradelog.backend.controller;

import com.tradelog.backend.dto.CommentDto;
import com.tradelog.backend.entity.Comment;
import com.tradelog.backend.service.CommentService;
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
@RequestMapping("/api/journals/{journalId}/comments")
@RequiredArgsConstructor
@Tag(name = "댓글 API", description = "매매일지 댓글 관련 API")
public class CommentController {
    private final CommentService commentService;

    @GetMapping
    @Operation(summary = "댓글 목록 조회", description = "특정 매매일지의 댓글 목록을 조회합니다.")
    public ResponseEntity<List<Comment>> getComments(@PathVariable Long journalId) {
        return ResponseEntity.ok(commentService.findByJournal(journalId));
    }

    @PostMapping
    @Operation(summary = "댓글 작성", description = "특정 매매일지에 댓글을 작성합니다.")
    public ResponseEntity<Comment> createComment(
            @PathVariable Long journalId,
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody CommentDto.CreateRequest request) {
        return ResponseEntity.ok(commentService.createComment(journalId, userDetails.getUsername(), request));
    }

    @PutMapping("/{commentId}")
    @Operation(summary = "댓글 수정", description = "작성한 댓글을 수정합니다.")
    public ResponseEntity<Comment> updateComment(
            @PathVariable Long journalId,
            @PathVariable Long commentId,
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody CommentDto.UpdateRequest request) {
        return ResponseEntity.ok(commentService.updateComment(commentId, userDetails.getUsername(), request));
    }

    @DeleteMapping("/{commentId}")
    @Operation(summary = "댓글 삭제", description = "작성한 댓글을 삭제합니다.")
    public ResponseEntity<Void> deleteComment(
            @PathVariable Long journalId,
            @PathVariable Long commentId,
            @AuthenticationPrincipal UserDetails userDetails) {
        commentService.deleteComment(commentId, userDetails.getUsername());
        return ResponseEntity.noContent().build();
    }
} 