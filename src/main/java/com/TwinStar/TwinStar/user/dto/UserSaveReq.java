package com.TwinStar.TwinStar.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class UserSaveReq {
    private String name;
    private String email;
    private String password;
}
