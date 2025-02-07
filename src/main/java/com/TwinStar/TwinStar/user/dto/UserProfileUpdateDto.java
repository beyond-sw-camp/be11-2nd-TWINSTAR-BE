package com.TwinStar.TwinStar.user.dto;

import com.TwinStar.TwinStar.user.domain.IdVisibility;
import com.TwinStar.TwinStar.user.domain.Sex;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class UserProfileUpdateDto {
    private String nickName;
    private String profileTxt;
    private Sex sex;
    private IdVisibility idVisibility;
}
