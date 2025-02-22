package com.TwinStar.TwinStar.report.dtos;

import com.TwinStar.TwinStar.report.domain.ReportStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ReportProcessRequestDto {

    @NotNull(message = "관리자 코멘트는 필수입니다.")
    private String comment; // 관리자 코멘트

    @NotNull(message = "처리 상태는 필수입니다.")
    private ReportStatus reportStatus; // 처리 상태 (PROCESSED)
}
