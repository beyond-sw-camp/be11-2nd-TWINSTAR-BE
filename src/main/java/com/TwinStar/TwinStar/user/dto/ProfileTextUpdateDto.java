package com.TwinStar.TwinStar.user.dto;

import com.TwinStar.TwinStar.common.domain.Visibility;
import com.TwinStar.TwinStar.user.domain.Sex;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import software.amazon.awssdk.services.s3.endpoints.internal.Value;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ProfileTextUpdateDto {
    private String nickName;
    private String profileTxt;
    private Visibility idVisibility;
    private Sex sex;
}
