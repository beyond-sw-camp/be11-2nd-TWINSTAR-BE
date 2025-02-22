package com.TwinStar.TwinStar.blackList.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import software.amazon.awssdk.services.s3.endpoints.internal.Value;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class BlockRequest {
    private Long userId;
    private Long blockedUserId;
    private Integer days; //차단 기간
}
