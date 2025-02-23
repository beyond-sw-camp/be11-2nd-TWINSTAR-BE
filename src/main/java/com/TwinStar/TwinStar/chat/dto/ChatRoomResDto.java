package com.TwinStar.TwinStar.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ChatRoomResDto {
    private Long roomId;
    private String roomName;
    private Long notReadCount;
    private String isGroupChat;
    private String roomImage;
    private Boolean isActive;
    private Long userCount;
    private LocalDateTime updatedTime;

    public ChatRoomResDto(Long roomId, String roomName, Long notReadCount, String isGroupChat, Boolean isActive, Long userCount, LocalDateTime updatedTime) {
        this.roomId = roomId;
        this.roomName = roomName;
        this.notReadCount = notReadCount;
        this.isGroupChat = isGroupChat;
        this.roomImage = null;  // 기본적으로 null, 이후 서비스에서 설정
        this.isActive = isActive;
        this.userCount = userCount;
        this.updatedTime = updatedTime;
    }
}
