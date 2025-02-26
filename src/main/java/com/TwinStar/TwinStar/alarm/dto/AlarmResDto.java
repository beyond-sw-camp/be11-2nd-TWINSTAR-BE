package com.TwinStar.TwinStar.alarm.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AlarmResDto {
    private Long id;
    private Long senderId;
    private String profileImage;
    private String content;
    private String url;
    private LocalDateTime createdTime;
}
