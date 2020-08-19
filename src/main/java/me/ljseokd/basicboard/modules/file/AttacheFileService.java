package me.ljseokd.basicboard.modules.file;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.ljseokd.basicboard.infra.config.AppProperties;
import me.ljseokd.basicboard.modules.file.dto.AttacheFileDto;
import me.ljseokd.basicboard.modules.notice.Notice;
import me.ljseokd.basicboard.modules.notice.NoticeRepository;
import org.apache.tika.Tika;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
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
    private final AttacheFileRepository attacheFileRepository;

    public void addAttacheFile(Long createdId, MultipartFile[] file) {
        Notice notice = noticeRepository.findById(createdId).get();
        Set<AttacheFile> attacheFiles = new HashSet<>();

        for (MultipartFile multipartFile : file) {
            if (!multipartFile.isEmpty()){
                AttacheFile attacheFile = newAttacheFile(multipartFile);
                attacheFiles.add(attacheFile);
                makeFile(multipartFile, attacheFile.getSaveFileName(), attacheFile.getPath());
            }
        }
        notice.addFiles(attacheFiles);
    }

    private AttacheFile newAttacheFile(MultipartFile multipartFile) {
        String orgFileName = multipartFile.getOriginalFilename();
        String ext = StringUtils.getFilenameExtension(orgFileName);
        String saveFileName = UUID.randomUUID().toString();
        Long fileSize = multipartFile.getSize();

        return new AttacheFile(orgFileName, ext, saveFileName, getPath(), fileSize);
    }

    private String getPath() {
        LocalDateTime now = LocalDateTime.now();

        String basePath = appProperties.getPath();
        String year = String.valueOf(now.getYear());
        String month = String.valueOf(now.getMonthValue());
        String day = String.valueOf(now.getDayOfMonth());

        Path path = Paths.get(basePath, year, month, day);
        return path.toString();
    }

    private void makeFile(MultipartFile multipartFile, String saveFileName, String path) {
        File file = getFileForPath(saveFileName, path);
        try {
            multipartFile.transferTo(file);
        } catch (IOException e) {
            log.error("게시글 첨부파일 추가 중 에러 : {}", e.getMessage());
        }
    }

    private File getFileForPath(String saveFileName, String path) {
        File file = new File(path);
        if (!file.exists()){
            file.mkdirs();
        }
        return new File(file ,   "/" + saveFileName);
    }

    public AttacheFileDto getResourceFile(Long fileId) throws IOException {
        AttacheFile attacheFile = attacheFileRepository.findById(fileId)
                .orElseThrow(() -> new IllegalArgumentException("해당 번호에 파일 데이터를 찾을 수 없습니다." + fileId));

        File file = new File(attacheFile.getPath() + "/" + attacheFile.getSaveFileName());

        return new AttacheFileDto(
                file,
                new InputStreamResource(new FileInputStream(file)),
                getMediaType(file),
                attacheFile.getOrgFileName()
        );
    }

    private MediaType getMediaType(File file) throws IOException {
        Tika tika = new Tika();
        String mimeType = tika.detect(file);
        MediaType mediaType = MediaType.parseMediaType(mimeType);
        return mediaType;
    }

    public void removeFile(Long fileId) {
        AttacheFile attacheFile = attacheFileRepository.findById(fileId)
                .orElseThrow(() -> new IllegalArgumentException("해당 첨부파일이 없습니다." + fileId));
        attacheFileRepository.delete(attacheFile);
        String path = attacheFile.getPath();
        String saveFileName = attacheFile.getSaveFileName();
        File removeFile = new File(path + "/" + saveFileName);
        if (removeFile.exists()){
            removeFile.delete();
        }
    }
}
