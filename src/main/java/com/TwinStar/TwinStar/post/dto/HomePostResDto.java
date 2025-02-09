package com.TwinStar.TwinStar.post.dto;

import com.TwinStar.TwinStar.common.domain.YN;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class HomePostResDto {
    private Long post_id;
    private Long sharePostId;
    private String nickName;
    private List<MultipartFile> fileList;
    private int postLikeCount;
    private int commentCount;
    private int shareCount;
    private LocalDateTime createdTime;
    private YN updatedStatus;
}