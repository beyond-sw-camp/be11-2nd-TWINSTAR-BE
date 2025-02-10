package com.TwinStar.TwinStar.post_file;

import com.TwinStar.TwinStar.post.domain.Post;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

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

        List<PostFile> postFiles = fileUrls.stream()
                .map(url -> PostFile.builder()
                        .post(post)
                        .fileUrl(url)
                        .fileType(getFileType(url)) // 파일 타입 결정
                        .build())
                .collect(Collectors.toList());

        postFileRepository.saveAll(postFiles);
    }

    private String getFileType(String url) {
        if (url.endsWith(".jpg") || url.endsWith(".jpeg") || url.endsWith(".png") || url.endsWith(".gif")) {
            return "image";
        } else if (url.endsWith(".mp4") || url.endsWith(".avi") || url.endsWith(".mov")) {
            return "video";
        } else {
            throw new IllegalArgumentException("지원되지 않는 파일 형식입니다.");
        }
    }
}
