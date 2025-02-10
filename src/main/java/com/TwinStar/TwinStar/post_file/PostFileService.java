package com.TwinStar.TwinStar.post_file;

import com.TwinStar.TwinStar.common.domain.YN;
import com.TwinStar.TwinStar.post.domain.Post;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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

        // 🔹 기존 파일 목록 조회 (숨김 여부 상관없이 전체 가져옴)
        List<PostFile> existingFiles = postFileRepository.findByPost(post);

        // 🔹 다시 보이게 할 파일 리스트 (기존에 존재하는데 다시 선택된 파일 → isHide = 'N'으로 변경)
        List<PostFile> filesToRestore = existingFiles.stream()
                .filter(file -> fileUrls.contains(file.getFileUrl()) && file.getIsHide() == YN.Y)
                .toList();

        // 🔹 새로운 파일 리스트 (기존에 없는 파일만 추가)
        List<String> existingFileUrls = existingFiles.stream()
                .map(PostFile::getFileUrl)
                .toList();

        List<PostFile> newFiles = fileUrls.stream()
                .filter(url -> !existingFileUrls.contains(url)) // 기존에 없는 파일만 추가
                .map(url -> PostFile.builder()
                        .post(post)
                        .fileUrl(url)
                        .fileType(getFileType(url)) // 파일 타입 결정
                        .isHide(YN.N) // 기본값은 보이게 설정
                        .build())
                .collect(Collectors.toList());

        // ✅ 기존 파일을 다시 선택했으면 복원
        for (PostFile file : filesToRestore) {
            file.restoreFile();
        }

        // ✅ 새 파일 저장
        if (!newFiles.isEmpty()) {
            postFileRepository.saveAll(newFiles);
        }
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

    // ✅ 기존 파일을 숨김 처리 (isHide = 'Y')
    public void hidePostFiles(List<String> fileUrls) {
        if (!fileUrls.isEmpty()) {
            postFileRepository.hideFilesByUrls(fileUrls);
        }
    }

    // ✅ 기존 파일을 다시 보이도록 처리 (isHide = 'N')
    public void restorePostFiles(List<String> fileUrls) {
        if (!fileUrls.isEmpty()) {
            postFileRepository.restoreFilesByUrls(fileUrls);
        }
    }
}
