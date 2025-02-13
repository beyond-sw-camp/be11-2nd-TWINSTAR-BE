package com.TwinStar.TwinStar.report.service;

import com.TwinStar.TwinStar.report.domain.Report;
import com.TwinStar.TwinStar.report.domain.ReportStatus;
import com.TwinStar.TwinStar.report.domain.Type;
import com.TwinStar.TwinStar.report.dtos.ReportProcessRequestDto;
import com.TwinStar.TwinStar.report.dtos.ReportRequestDto;
import com.TwinStar.TwinStar.report.dtos.ReportResponseDto;
import com.TwinStar.TwinStar.report.repository.ReportRepository;
import com.TwinStar.TwinStar.user.domain.User;
import com.TwinStar.TwinStar.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ReportService {
    private final ReportRepository reportRepository;
    private final UserRepository userRepository;

    public ReportService(ReportRepository reportRepository, UserRepository userRepository) {
        this.reportRepository = reportRepository;
        this.userRepository = userRepository;
    }

//    일반 유저가 신고
    public void reportUser(ReportRequestDto dto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long reporterId = Long.valueOf(authentication.getName());
        User reporter = userRepository.findById(reporterId).orElseThrow(()->new EntityNotFoundException("사용자를 찾을 수 없습니다."));
        if (reporterId == dto.getReportedId()) {
            throw new IllegalArgumentException("자기 자신을 신고할 수 없습니다.");
        }

        User reported = userRepository.findById(dto.getReportedId())
                .orElseThrow(() -> new IllegalArgumentException("신고 대상 사용자가 존재하지 않습니다."));

//        중복신고 방지(신고자id, 신고당한유저id,신고유형,신고id where 조건 걸어서 필터링)
        if (reportRepository.findByReporterIdAndReportedIdAndReportedTypeAndTypeId(
                reporterId, dto.getReportedId(), dto.getReportType(), dto.getTypeId()).isPresent()) {
            throw new IllegalStateException("이미 신고한 사용자입니다.");
        }

        Report report = Report.builder()
                .reporter(reporter)
                .reported(reported)
                .reportType(dto.getReportType())
                .typeId(dto.getTypeId())
                .content(dto.getContent())
                .reportStatus(ReportStatus.PENDING)
                .build();

        reportRepository.save(report);
    }

    // 특정 신고 상세 조회
    public ReportResponseDto getReportDetails(Long reportId) {
        Report report = reportRepository.findById(reportId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 신고입니다."));
        return new ReportResponseDto(report);
    }

    //  특정 신고 유형 목록 조회
    public List<ReportResponseDto> getReportsByType(Type reportedType) {
        List<Report> reports = reportRepository.findByReportedType(reportedType);
        return reports.stream().map(ReportResponseDto::new).collect(Collectors.toList());
    }

    //  관리자 신고 처리 기능
    public void processReport(Long reportId, ReportProcessRequestDto processDto) {
        Report report = reportRepository.findById(reportId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 신고입니다."));

        // 신고 상태 변경 & 관리자 코멘트 추가
        report.updateStatusAndDelete(processDto.getReportStatus(), processDto.getComment());
        reportRepository.save(report);
    }
}
