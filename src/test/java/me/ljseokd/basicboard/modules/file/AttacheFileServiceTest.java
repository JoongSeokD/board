package me.ljseokd.basicboard.modules.file;

import me.ljseokd.basicboard.infra.AbstractContainerBaseTest;
import me.ljseokd.basicboard.infra.config.AppProperties;
import me.ljseokd.basicboard.modules.account.Account;
import me.ljseokd.basicboard.modules.account.AccountRepository;
import me.ljseokd.basicboard.modules.account.WithAccount;
import me.ljseokd.basicboard.modules.notice.NoticeService;
import me.ljseokd.basicboard.modules.notice.form.NoticeForm;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ActiveProfiles("dev")
class AttacheFileServiceTest extends AbstractContainerBaseTest {

    @Autowired AttacheFileService attacheFileService;

    @Autowired NoticeService noticeService;

    @Autowired AccountRepository accountRepository;
    @Autowired AppProperties appProperties;


    @DisplayName("첨부파일 저장")
    @Test
    @WithAccount("ljseokd")
    void save_attache_file() throws Exception {
        //given
        Account ljseokd = accountRepository.findByNickname("ljseokd").get();
        NoticeForm noticeForm = new NoticeForm();
        noticeForm.setTitle("saveTitle");
        noticeForm.setContents("saveContents");
        Long noticeId = noticeService.createNotice(ljseokd, noticeForm);
        MultipartFile[] multipartFiles = {new MockMultipartFile("file", "testFile", "text/plain", "testFile".getBytes())};

        //when
        attacheFileService.addAttacheFile(noticeId, multipartFiles);

        LocalDateTime now = LocalDateTime.now();
        String basePath = appProperties.getPath();
        String year = String.valueOf(now.getYear());
        String month = String.valueOf(now.getMonthValue());
        String day = String.valueOf(now.getDayOfMonth());

        Path path = Paths.get(basePath, year, month, day);
        File file = new File(path.toString());
        //then

        assertTrue(file.exists());
    }

}