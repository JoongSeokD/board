package me.ljseokd.basicboard.controller;

import me.ljseokd.basicboard.WithAccount;
import me.ljseokd.basicboard.domain.Account;
import me.ljseokd.basicboard.domain.Notice;
import me.ljseokd.basicboard.form.NoticeForm;
import me.ljseokd.basicboard.form.SignUpForm;
import me.ljseokd.basicboard.repository.AccountRepository;
import me.ljseokd.basicboard.repository.NoticeRepository;
import me.ljseokd.basicboard.service.AccountService;
import me.ljseokd.basicboard.service.NoticeService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest
@Transactional
class NoticeControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    AccountService accountService;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    NoticeService noticeService;

    @Autowired
    NoticeRepository noticeRepository;

    @DisplayName("게시글 등록 페이지")
    @Test
    @WithAccount("ljseokd")
    void add_notice_form() throws Exception {
        mockMvc.perform(get("/notice/new"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("noticeForm"))
                .andExpect(view().name("notice/create-notice-form"))
                .andExpect(authenticated());
    }

    @DisplayName("게시글 등록 성공")
    @Test
    @WithAccount("ljseokd")
    void create_notice_success() throws Exception {
        mockMvc.perform(post("/notice/new")
                .param("title", "타이틀")
                .param("contents", "내용")
                .with(csrf()))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(authenticated());
    }

    @DisplayName("게시글 등록 실패 (제목 길이 초과)")
    @Test
    @WithAccount("ljseokd")
    void create_notice_fail() throws Exception {
        mockMvc.perform(post("/notice/new")
                .param("title", "타이틀타이틀타이틀타이틀타이틀타이틀타이틀타이틀타이틀타이틀타이틀타이틀타이틀타이틀타이틀타이틀타이틀타이틀타이틀타이틀타이틀타이틀타이틀타이틀타이틀타이틀타이틀타이틀타이틀타이틀타이틀타이틀타이틀타이틀타이틀타이틀타이틀타이틀타이틀타이틀타이틀타이틀타이틀타이틀타이틀타이틀")
                .param("contents", "내용")
                .with(csrf()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(model().hasErrors())
                .andExpect(model().attributeExists("noticeForm"))
                .andExpect(view().name("/notice/create-notice-form"))
                .andExpect(authenticated());
    }

    @DisplayName("게시글 보기 성공")
    @Test
    void view_notice_success() throws Exception {
        //given
        SignUpForm signUpForm = new SignUpForm();
        signUpForm.setUsername("ljseokd");
        signUpForm.setPassword("12341234");
        Long createdId = accountService.join(signUpForm);
        Account account = accountRepository.findById(createdId).get();

        NoticeForm noticeForm = new NoticeForm();
        noticeForm.setTitle("title");
        noticeForm.setContents("contents");
        Long noticeId = noticeService.createNotice(account, noticeForm);
        Notice notice = noticeRepository.findById(noticeId).get();

        //when
        mockMvc.perform(get("/notice/" + noticeId + "/view"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("isWriter"))
                .andExpect(model().attributeExists("notice"))
                .andExpect(view().name("notice/view"));

        //then
        assertTrue(notice.isWriter(account));
    }

    @DisplayName("게시글 보기 실패 (없는 경로)")
    @Test
    void view_notice_fail() throws Exception {
        assertThrows(Exception.class,
                () -> mockMvc.perform(get("/notice/123/view"))) ;
    }

}