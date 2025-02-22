package com.TwinStar.TwinStar.report.controller;

import com.TwinStar.TwinStar.common.dto.CommonDto;
import com.TwinStar.TwinStar.report.domain.Type;
import com.TwinStar.TwinStar.report.dtos.ReportProcessRequestDto;
import com.TwinStar.TwinStar.report.dtos.ReportProcessResponseDto;
import com.TwinStar.TwinStar.report.dtos.ReportRequestDto;
import com.TwinStar.TwinStar.report.dtos.ReportResponseDto;
import com.TwinStar.TwinStar.report.service.ReportService;
import com.TwinStar.TwinStar.user.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reports")
public class ReportController {
    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @PostMapping("/receipt") //신고접수
        public ResponseEntity<?> reportUser(@RequestBody @Valid ReportRequestDto dto) {
            reportService.reportUser(dto);
            return new ResponseEntity<>(new CommonDto(HttpStatus.OK.value(),"신고가 접수되었습니다.", null),HttpStatus.OK);
    }

    // 신고 목록 조회
    @GetMapping("/list")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getReports(Pageable pageable, ReportRequestDto dto) {
        Page<ReportResponseDto> reports = reportService.findAllReports(pageable, dto);
        return new ResponseEntity<>(new CommonDto(HttpStatus.OK.value(), "신고 유저 조회 성공",reports),HttpStatus.OK);
    }

    // 특정 신고 상세 조회
    @GetMapping("/detail/{reportId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getReportDetails(@PathVariable Long reportId) {
        ReportProcessResponseDto reportDetails = reportService.getReportDetails(reportId);
        return new ResponseEntity<>(new CommonDto(HttpStatus.OK.value(), "신고 유저 상세 조회 성공",reportDetails),HttpStatus.OK);
    }

    //특정 신고 유형 목록 조회
    @GetMapping("/type/{reportType}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getReportsByType(@PathVariable Type reportType) {
        List<ReportResponseDto> reports = reportService.getReportsByType(reportType);
        return new ResponseEntity<>(new CommonDto(HttpStatus.OK.value(), "신고 유형 조회 성공",reports),HttpStatus.OK);
    }

    // 관리자가 특정 신고 처리
    @PutMapping("/{reportId}/process-delete")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> processReport(@PathVariable Long reportId, //신고테이블의 기본키인 신고아이디
                                           @RequestBody @Valid ReportProcessRequestDto processDto) {

        reportService.processReport(reportId, processDto);
        return new ResponseEntity<>(new CommonDto(HttpStatus.OK.value(),"신고가 처리 후 삭제되었습니다.",processDto),HttpStatus.OK);
    }
}
