package com.TwinStar.TwinStar.report.dtos;

import com.TwinStar.TwinStar.report.domain.Report;
import com.TwinStar.TwinStar.report.domain.ReportStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@Builder
public class ReportProcessResponseDto {
    private Long reportId;
    private Long reportedId;
    private String adminComment;
    private ReportStatus reportStatus;

    public static ReportProcessResponseDto reportProcessResponseDto(Report report){
        return ReportProcessResponseDto.builder()
                .reportId(report.getId())
                .reportedId(report.getReported().getId())
                .adminComment(report.getComment())
                .reportStatus(report.getReportStatus())
                .build();
    }
}
