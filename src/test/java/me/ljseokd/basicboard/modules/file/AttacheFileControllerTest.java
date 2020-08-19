package me.ljseokd.basicboard.modules.file;

import me.ljseokd.basicboard.infra.AbstractContainerBaseTest;
import me.ljseokd.basicboard.infra.MockMvcTest;
import me.ljseokd.basicboard.modules.account.Account;
import me.ljseokd.basicboard.modules.account.AccountRepository;
import me.ljseokd.basicboard.modules.account.WithAccount;
import me.ljseokd.basicboard.modules.notice.Notice;
import me.ljseokd.basicboard.modules.notice.NoticeRepository;
import me.ljseokd.basicboard.modules.notice.NoticeService;
import me.ljseokd.basicboard.modules.notice.form.NoticeForm;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@MockMvcTest
class AttacheFileControllerTest extends AbstractContainerBaseTest {

    @Autowired AttacheFileService attacheFileService;
    @Autowired NoticeService noticeService;
    @Autowired NoticeRepository noticeRepository;
    @Autowired AccountRepository accountRepository;
    @Autowired MockMvc mockMvc;

    @DisplayName("첨부파일 삭제 성공")
    @Test
    @WithAccount("ljseokd")
    void remove_attache_file_success() throws Exception { 
        //given 
        Account ljseokd = accountRepository.findByNickname("ljseokd").get();
        NoticeForm noticeForm = new NoticeForm();
        noticeForm.setTitle("saveTitle");
        noticeForm.setContents("saveContents");
        Long noticeId = noticeService.createNotice(ljseokd, noticeForm);
        MultipartFile[] multipartFiles = {new MockMultipartFile("file", "testFile", "text/plain", "testFile".getBytes())};

        //when
        attacheFileService.addAttacheFile(noticeId, multipartFiles);
        Notice notice = noticeRepository.findByIdFetchFileAndReply(noticeId).get();
        AttacheFile attacheFile = notice.getFileList().stream().collect(Collectors.toList()).get(0);
        String filePath = attacheFile.getPath() + "/" + attacheFile.getSaveFileName();
        File saveFile = new File(filePath);

        //then
        assertTrue(saveFile.exists());
        mockMvc.perform(post("/attacheFile/" + attacheFile.getId() + "/remove")
                .with(csrf()))
                .andExpect(status().isOk());
        assertFalse(saveFile.exists());
    }

    @DisplayName("첨부파일 삭제 실패")
    @Test
    @WithAccount("ljseokd")
    void remove_attache_file_fail() throws Exception {
        assertThrows(Exception.class,
                () -> mockMvc.perform(post("/attacheFile/1/remove")
                        .with(csrf())));
    }


}