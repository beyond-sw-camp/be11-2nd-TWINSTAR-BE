package com.TwinStar.TwinStar.report.domain;

import com.TwinStar.TwinStar.blackList.dto.BlockRequest;
import com.TwinStar.TwinStar.report.dtos.ReportProcessResponseDto;
import com.TwinStar.TwinStar.report.dtos.ReportRequestDto;
import com.TwinStar.TwinStar.report.dtos.ReportResponseDto;
import com.TwinStar.TwinStar.user.domain.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "report")
@Getter
@Transactional
@SQLDelete(sql = "UPDATE report SET is_deleted = true WHERE id = ?") //특정ID를 소프트딜리트 함
@EntityListeners(AuditingEntityListener.class)
public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
//    신고한 유저를 추가로 넣은 이유는 레파지토리에서 목록을 찾기 더 쉽고(네이밍 규칙) 코드 볼 때 직관적인 것 같아서 넣었습니다.
    @JoinColumn(name = "reporter_id", nullable = false) //신고한 유저
    private User reporter; // 신고한 사용자
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reported_id", nullable = false)//신고 당한 유저
    private User reported;
    private String content; //신고 사유
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Type reportType;
    @Column(nullable = false)
    private Long typeId;
    @CreationTimestamp
    private LocalDateTime reportedTime;
    @LastModifiedBy //엔티티가 수정될 때 자동으로 갱신. 레파지토리에서 save()해야만 자동갱신
    private LocalDateTime processedAt;//updatedTime에서 processedAt로 변수명 변경 , 자동으로 갱신
    private String comment; //관리자 멘트
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private ReportStatus reportStatus = ReportStatus.PENDING;
    @Column(nullable = false)
    @Builder.Default
    private Boolean isDeleted = false;


    // 신고 상태 변경 메서드
    public void updateStatusAndDelete(ReportStatus status, String comment) {
        this.reportStatus = status;
        this.comment = comment;
        this.processedAt = LocalDateTime.now(); // 처리된 시간 갱신
        this.isDeleted = true;
    }

}
