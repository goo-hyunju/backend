package com.tradelog.backend.dto.journal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JournalRequest {

    @NotBlank(message = "종목 심볼은 필수 입력값입니다.")
    private String stockSymbol;

    @NotNull(message = "매수일은 필수 입력값입니다.")
    private LocalDate buyDate;

    private LocalDate sellDate;

    @NotNull(message = "수익률은 필수 입력값입니다.")
    private Double profitRate;

    private String strategyDescription;

    private String emotion;

    @NotNull(message = "공개 여부는 필수 입력값입니다.")
    private Boolean isPublic;
} 