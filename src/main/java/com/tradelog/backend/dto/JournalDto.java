package com.tradelog.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

public class JournalDto {

    @Getter
    @Setter
    public static class CreateRequest {
        @NotBlank(message = "종목 코드는 필수 입력값입니다.")
        private String stockSymbol;

        @NotNull(message = "매수일은 필수 입력값입니다.")
        private LocalDateTime buyDate;

        @NotNull(message = "매도일은 필수 입력값입니다.")
        private LocalDateTime sellDate;

        @NotNull(message = "수익률은 필수 입력값입니다.")
        private Double profitRate;

        private String strategyDescription;
        private String emotion;

        @NotNull(message = "공개 여부는 필수 입력값입니다.")
        private Boolean isPublic;
    }

    @Getter
    @Setter
    public static class UpdateRequest {
        @NotBlank(message = "종목 코드는 필수 입력값입니다.")
        private String stockSymbol;

        @NotNull(message = "매수일은 필수 입력값입니다.")
        private LocalDateTime buyDate;

        @NotNull(message = "매도일은 필수 입력값입니다.")
        private LocalDateTime sellDate;

        @NotNull(message = "수익률은 필수 입력값입니다.")
        private Double profitRate;

        private String strategyDescription;
        private String emotion;

        @NotNull(message = "공개 여부는 필수 입력값입니다.")
        private Boolean isPublic;
    }

    @Getter
    @Setter
    public static class Response {
        private Long id;
        private Long userId;
        private String userNickname;
        private String stockSymbol;
        private LocalDateTime buyDate;
        private LocalDateTime sellDate;
        private Double profitRate;
        private String strategyDescription;
        private String emotion;
        private Boolean isPublic;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
    }
} 