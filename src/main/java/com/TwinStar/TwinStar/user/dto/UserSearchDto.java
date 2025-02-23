package com.TwinStar.TwinStar.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class UserSearchDto {
    private Long id;
    private String nickName;
    private String email;

    public UserSearchDto(Long id, String nickName, String email) {
        this.id = id;
        this.nickName = nickName;
        this.email = email;
    }
}
