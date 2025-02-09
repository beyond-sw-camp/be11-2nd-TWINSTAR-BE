package com.TwinStar.TwinStar.user.dto;

import com.TwinStar.TwinStar.user.domain.IdVisibility;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ChangeIdVisibility {
    private IdVisibility idVisibility;
}
