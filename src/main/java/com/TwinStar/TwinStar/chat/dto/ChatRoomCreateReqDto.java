package com.TwinStar.TwinStar.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ChatRoomCreateReqDto {
    private List<Long> idList ;
    private String chatRoomName;
}
