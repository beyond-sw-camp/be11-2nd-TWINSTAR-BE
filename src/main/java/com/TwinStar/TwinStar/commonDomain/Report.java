package com.TwinStar.TwinStar.commonDomain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
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
@EntityListeners(AuditingEntityListener.class)
public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User userId;
    private String content;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Type reportType;
    @Column(nullable = false)
    private String typeId;
    @CreationTimestamp
    private LocalDateTime createdTime;
    @LastModifiedBy //엔티티가 수정될 때 자동으로 갱신. 레파지토리에서 save()해야만 자동갱신
    private LocalDateTime processedAt;//updatedTime에서 processedAt로 변수명 변경
    @Column(nullable = false)
    private String comment;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReportStatus reportStatus;
}
