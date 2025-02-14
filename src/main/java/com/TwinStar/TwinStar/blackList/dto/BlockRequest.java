package com.TwinStar.TwinStar.blackList.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class BlockRequest {
    private Long userId;
    private Long blockedUserId;
}
