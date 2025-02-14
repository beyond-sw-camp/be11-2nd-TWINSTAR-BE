package com.TwinStar.TwinStar.report.repository;

import com.TwinStar.TwinStar.report.domain.Report;
import com.TwinStar.TwinStar.report.domain.Type;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReportRepository extends JpaRepository<Report,Long> {

    // 특정 사용자가 특정 사용자를 신고한 내역이 있는지 확인
    Optional<Report> findByReporterIdAndReportedIdAndReportTypeAndTypeId(
            Long reporterId, Long reportedId, Type reportedType, Long typeId);

    // 특정 신고 유형 목록 조회
    List<Report> findByReportType(Type reportType);

//    // 삭제된 신고도 포함하여 전체 조회 (관리자용)
//    @Query("SELECT r FROM Report r WHERE r.reportedType = :reportedType")
//    List<Report> findAllReportsIncludingDeleted(@Param("reportedType") Type reportedType);
//
//    //
}
