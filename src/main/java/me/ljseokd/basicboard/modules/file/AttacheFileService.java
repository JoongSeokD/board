package me.ljseokd.basicboard.modules.file;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.ljseokd.basicboard.infra.config.AppProperties;
import me.ljseokd.basicboard.modules.notice.Notice;
import me.ljseokd.basicboard.modules.notice.NoticeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class AttacheFileService {

    private final AppProperties appProperties;
    private final NoticeRepository noticeRepository;

    public void addAttacheFile(Long createdId, MultipartFile[] file) {
        Notice notice = noticeRepository.findById(createdId).get();
        Set<AttacheFile> attacheFiles = new HashSet<>();

        for (MultipartFile multipartFile : file) {
            if (!multipartFile.isEmpty()){
                AttacheFile attacheFile = getAttacheFile(multipartFile);
                attacheFiles.add(attacheFile);
                makeFile(multipartFile, attacheFile.getSaveFileName());
            }
        }
        notice.addFiles(attacheFiles);
    }

    private AttacheFile getAttacheFile(MultipartFile multipartFile) {
        String orgFileName = multipartFile.getOriginalFilename();
        String ext = StringUtils.getFilenameExtension(orgFileName);
        String saveFileName = UUID.randomUUID().toString();
        Long fileSize = multipartFile.getSize();

        return new AttacheFile(orgFileName, ext, saveFileName, fileSize);
    }

    private void makeFile(MultipartFile multipartFile, String saveFileName) {
        LocalDateTime now = LocalDateTime.now();

        String basePath = appProperties.getPath();
        String year = String.valueOf(now.getYear());
        String month = String.valueOf(now.getMonthValue());
        String day = String.valueOf(now.getDayOfMonth());

        Path path = Paths.get(basePath, year, month, day);
        File file = new File(path.toString());

        if (!file.exists()){
            file.mkdirs();
        }
        file = new File(file ,   "/" +  saveFileName);

        try {
            multipartFile.transferTo(file);
        } catch (IOException e) {
            log.error("게시글 첨부파일 추가 중 에러 : {}", e.getMessage());
        }
    }
}
