package com.TwinStar.TwinStar.report.dtos;

import com.TwinStar.TwinStar.report.domain.Report;
import com.TwinStar.TwinStar.report.domain.ReportStatus;
import com.TwinStar.TwinStar.report.domain.Type;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@Data
@Builder
public class ReportResponseDto {
    private Long id; // 신고 ID
    private Long reporterId; // 신고한 사용자 ID
    private Long reportedId; // 신고당한 사용자 ID
    private Type reportedType; // 신고 대상 유형 (USER, POST, COMMENT)
    private Long typeId; // 신고 대상의 ID (게시글 ID, 댓글 ID, 사용자 ID 중 하나)
    private String content; // 신고 사유
    private ReportStatus reportStatus;
    private LocalDateTime reportedTime; // 신고된 시간

    public ReportResponseDto(Report report) {
        this.id = report.getId();
        this.reporterId = report.getReporter().getId();
        this.reportedId = report.getReported().getId();
        this.reportedType = report.getReportType();
        this.typeId = report.getTypeId();
        this.content = report.getContent();
        this.reportStatus = report.getReportStatus();
        this.reportedTime = report.getReportedTime();
    }

    public static ReportResponseDto fromEntity(Report report) {
        return ReportResponseDto.builder()
                .id(report.getId())
                .reporterId(report.getReporter().getId())
                .reportedId(report.getReported().getId())
                .reportedType(report.getReportType())
                .typeId(report.getTypeId())
                .content(report.getContent())
                .reportedTime(report.getReportedTime())
                .reportStatus(report.getReportStatus())
                .build();
    }
}
