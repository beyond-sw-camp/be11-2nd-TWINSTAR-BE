package com.TwinStar.TwinStar.post_file;

import com.TwinStar.TwinStar.post.domain.Post;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class PostFileService {
    private final PostFileRepository postFileRepository;

    public PostFileService(PostFileRepository postFileRepository) {
        this.postFileRepository = postFileRepository;
    }


    public void savePostFiles(Post post, List<String> fileUrls) {
        if (fileUrls == null || fileUrls.isEmpty()) {
            throw new IllegalArgumentException("최소 1개 이상의 파일을 업로드해야 합니다.");
        }

        if (fileUrls.size() > 10) {
            throw new IllegalArgumentException("최대 10개까지 업로드할 수 있습니다.");
        }

        List<PostFile> newFiles = fileUrls.stream()
                .map(url -> PostFile.builder()
                        .post(post)
                        .fileUrl(url)
                        .fileType(getFileType(url))
                        .isHide("N")
                        .build())
                .toList();

        postFileRepository.saveAll(newFiles);
    }

    public String getFileType(String fileUrl) {
        String url = fileUrl.toLowerCase();

        if (url.endsWith(".jpg") || url.endsWith(".jpeg") || url.endsWith(".png") || url.endsWith(".gif")) {
            return "image";
        } else if (url.endsWith(".mp4") || url.endsWith(".avi") || url.endsWith(".mov")) {
            return "video";
        } else {
            throw new IllegalArgumentException("지원되지 않는 파일 형식입니다.");
        }
    }

    // ✅ 기존 파일을 숨김/복구 처리 (isHide = 'Y' 또는 'N')
    public void updateFileVisibility(Long postId, List<String> fileUrls, String isHideValue) {
        if (fileUrls == null || fileUrls.isEmpty()) {
            return; // 빈 리스트일 경우 업데이트 실행 X
        }
        postFileRepository.updateFileVisibility(fileUrls, postId, isHideValue);
    }
}