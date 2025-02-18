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

    // 기존 파일을 숨김 처리 (isHide = 'Y')
    public void hidePostFiles(Long postId, List<String> fileUrls) {
        if (!fileUrls.isEmpty()) {
            postFileRepository.hideFilesByUrls(fileUrls, postId, "Y"); // 🔥 String 값 직접 전달
        }
    }

    // 기존 파일을 다시 보이도록 처리 (isHide = 'N')
    public void restorePostFiles(Long postId, List<String> fileUrls) {
        if (!fileUrls.isEmpty()) {
            postFileRepository.restoreFilesByUrls(fileUrls, postId, "N"); // 🔥 String 값 직접 전달
        }
    }


    public List<PostFile> convertToPostFiles(Post post, List<String> fileUrls) {
        if (fileUrls == null || fileUrls.isEmpty()) {
            return new ArrayList<>();
        }

        return fileUrls.stream()
                .map(url -> PostFile.builder()
                        .post(post)
                        .fileUrl(url)
                        .fileType(getFileType(url))
                        .isHide("N")
                        .build())
                .toList();
    }
}