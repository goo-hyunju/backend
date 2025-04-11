package com.tradelog.backend.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

public class CommentDto {

    @Getter
    @Setter
    public static class CreateRequest {
        @NotBlank(message = "댓글 내용은 필수 입력값입니다.")
        private String content;
    }

    @Getter
    @Setter
    public static class UpdateRequest {
        @NotBlank(message = "댓글 내용은 필수 입력값입니다.")
        private String content;
    }

    @Getter
    @Setter
    public static class Response {
        private Long id;
        private Long journalId;
        private Long userId;
        private String userNickname;
        private String content;
        private LocalDateTime createdAt;
    }
} 