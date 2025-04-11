package com.tradelog.backend.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

public class AiFeedbackDto {

    @Getter
    @Setter
    public static class CreateRequest {
        @NotBlank(message = "피드백 내용은 필수 입력값입니다.")
        private String content;
        private String strengthPoints;
        private String weaknessPoints;
        private String improvementSuggestions;
    }

    @Getter
    @Setter
    public static class Response {
        private Long id;
        private Long journalId;
        private String content;
        private String strengthPoints;
        private String weaknessPoints;
        private String improvementSuggestions;
        private LocalDateTime createdAt;
    }
} 