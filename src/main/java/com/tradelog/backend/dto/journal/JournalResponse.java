package com.tradelog.backend.dto.journal;

import com.tradelog.backend.dto.comment.CommentResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JournalResponse {
    private Long id;
    private Long userId;
    private String userNickname;
    private String stockSymbol;
    private LocalDate buyDate;
    private LocalDate sellDate;
    private Double profitRate;
    private String strategyDescription;
    private String emotion;
    private Boolean isPublic;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<CommentResponse> comments;
    private AiFeedbackResponse aiFeedback;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AiFeedbackResponse {
        private Long id;
        private String content;
        private LocalDateTime createdAt;
    }
} 