package com.TwinStar.TwinStar.report.dtos;

import com.TwinStar.TwinStar.report.domain.Report;
import com.TwinStar.TwinStar.report.domain.ReportStatus;
import com.TwinStar.TwinStar.report.domain.Type;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
//클라이언트에서 신고받을 때 사용하는 dto
public class ReportRequestDto {
    @NotNull(message = "신고 대상 사용자의 ID는 필수입니다.")
    private Long reportedId;

    @NotNull(message = "신고 유형은 필수입니다.")
    private Type reportType; // 신고 유형

    @NotNull(message = "관련 ID는 필수입니다.")
    private Long typeId; // 신고 관련 ID (예: 게시글 ID, 댓글 ID)

    private ReportStatus reportStatus;

    private String content; // 신고 사유



}
