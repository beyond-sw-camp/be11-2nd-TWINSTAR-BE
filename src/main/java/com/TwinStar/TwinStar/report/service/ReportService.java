package com.TwinStar.TwinStar.report.service;

import com.TwinStar.TwinStar.report.domain.Report;
import com.TwinStar.TwinStar.report.domain.ReportStatus;
import com.TwinStar.TwinStar.report.domain.Type;
import com.TwinStar.TwinStar.report.dtos.ReportProcessRequestDto;
import com.TwinStar.TwinStar.report.dtos.ReportProcessResponseDto;
import com.TwinStar.TwinStar.report.dtos.ReportRequestDto;
import com.TwinStar.TwinStar.report.dtos.ReportResponseDto;
import com.TwinStar.TwinStar.report.repository.ReportRepository;
import com.TwinStar.TwinStar.user.domain.User;
import com.TwinStar.TwinStar.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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
        if (reportRepository.findByReporterIdAndReportedIdAndReportTypeAndTypeId(
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

//   신고 목록 조회
    public Page<ReportResponseDto> findAllReports(Pageable pageable,ReportRequestDto dto) {
        Specification<Report> spec = new Specification<Report>() {
            @Override
            public Predicate toPredicate(Root<Report> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
//                root : 엔티티의 속성을 접근하기 위한 객체, criteriabuilder : 쿼리를 생성하기 위한 객체
                List<Predicate> predicates = new ArrayList<>();

                // 신고 유형 필터링 (예: SPAM, ABUSE 등)
                if (dto.getReportType() != null){
                    predicates.add(criteriaBuilder.equal(root.get("reportType"),dto.getReportType()));
                }

                // 신고 상태 필터링 추가
                if (dto.getReportStatus() != null) {
                    predicates.add(criteriaBuilder.equal(root.get("reportStatus"), dto.getReportStatus()));
                }

                // 예: 신고 대상 유저 ID로 검색
                if (dto.getReportedId() != null) {
                    predicates.add(criteriaBuilder.equal(root.get("reportedId").get("id"), dto.getReportedId()));
                }

                // AND 조건 결합
                Predicate[] predicateArr = new Predicate[predicates.size()];
                for (int i =0; i<predicates.size();i++){
                    predicateArr[i] = predicates.get(i);
                }
                Predicate predicate = criteriaBuilder.and(predicateArr);
                return predicate;
            }
        };
            // 페이징과 검색을 함께 적용하여 조회
            Page<Report> reportPage = reportRepository.findAll(spec, pageable);
        return reportPage.map(ReportResponseDto::fromEntity);
    }

    // 특정 신고 상세 조회
    public ReportProcessResponseDto getReportDetails(Long reportId) {
        Report report = reportRepository.findById(reportId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 신고입니다."));
        return ReportProcessResponseDto.reportProcessResponseDto(report);
    }

    //  특정 신고 유형 목록 조회
    public List<ReportResponseDto> getReportsByType(Type reportedType) {
        List<Report> reports = reportRepository.findByReportType(reportedType);
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
