package me.ljseokd.basicboard.modules.notice;

import lombok.extern.slf4j.Slf4j;
import me.ljseokd.basicboard.infra.AbstractContainerBaseTest;
import me.ljseokd.basicboard.infra.MockMvcTest;
import me.ljseokd.basicboard.modules.account.Account;
import me.ljseokd.basicboard.modules.account.AccountRepository;
import me.ljseokd.basicboard.modules.account.AccountService;
import me.ljseokd.basicboard.modules.account.WithAccount;
import me.ljseokd.basicboard.modules.main.form.SignUpForm;
import me.ljseokd.basicboard.modules.notice.form.NoticeForm;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

import javax.persistence.EntityManager;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Slf4j
@MockMvcTest
class NoticeControllerTest  extends AbstractContainerBaseTest {

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

    @Autowired
    EntityManager entityManager;

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
                .andExpect(model().attributeExists("isOwner"))
                .andExpect(model().attributeExists("notice"))
                .andExpect(view().name("notice/view"));

        //then
        assertTrue(notice.getAccount().isOwner(account));
    }

    @DisplayName("게시글 보기 실패 (없는 경로)")
    @Test
    void view_notice_fail() throws Exception {
        assertThrows(Exception.class,
                () -> mockMvc.perform(get("/notice/123/view"))) ;
    }
    
    @DisplayName("게시글 수정 페이지")
    @Test
    void update_notice_form() throws Exception {
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
        //when
        mockMvc.perform(get("/notice/"+ noticeId +"/update"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("noticeForm"))
                .andExpect(view().name("notice/update"))
                .andExpect(result ->
                        assertTrue(result.getResponse().getContentAsString().contains("action=\"/notice/" + noticeId + "/update")))
        .andDo(print());

    }

    @DisplayName("게시글 수정 없는 페이지")
    @Test
    @WithAccount("ljseokd")
    void update_notice_form_fail() throws Exception {
        assertThrows(Exception.class,
                () -> mockMvc.perform(get("/notice/123/update"))) ;
    }

    @DisplayName("게시글 수정 성공")
    @Test
    void update_notice_success() throws Exception {
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
        Notice createNotice = noticeRepository.findById(noticeId).get();
        entityManager.flush();
        entityManager.clear();
        //when
        mockMvc.perform(post("/notice/" + noticeId + "/update")
                .param("title","updateTitle")
                .param("contents", "updateContents")
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(model().hasNoErrors())
                .andExpect(view().name("redirect:/notice/" + noticeId + "/view"));
        //then
        Notice findNotice = noticeRepository.findById(noticeId).get();
        log.info("createNotice.getTitle() : {}",createNotice.getTitle());
        log.info("findNotice.getTitle() : {}",findNotice.getTitle());
        assertNotEquals(createNotice.getTitle(), findNotice.getTitle());
        assertNotEquals(createNotice.getContents(), findNotice.getContents());
        assertNotEquals(createNotice.getLastModifiedTime(), findNotice.getLastModifiedTime());
    }

    @DisplayName("게시글 수정 실패 (제목 길이 초과)")
    @Test
    void update_notice_fail() throws Exception {
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
        //when
        mockMvc.perform(post("/notice/" + noticeId + "/update")
                .param("title","updateTitleupdateTitleupdateTitleupdateTitleupdateTitleupdateTitleupdateTitleupdateTitleupdateTitleupdateTitleupdateTitleupdateTitleupdateTitleupdateTitleupdateTitleupdateTitleupdateTitleupdateTitleupdateTitleupdateTitleupdateTitleupdateTitleupdateTitleupdateTitleupdateTitleupdateTitle")
                .param("contents", "updateContents")
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("noticeForm"))
                .andExpect(model().hasErrors())
                .andExpect(view().name("notice/update"));
    }

    @DisplayName("게시글 삭제 성공")
    @Test
    void delete_notice_success() throws Exception {
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
        //when
        mockMvc.perform(post("/notice/" + noticeId + "/delete")
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(flash().attributeExists("message"))
                .andExpect(view().name("redirect:/notice/list"));
    }

    @DisplayName("게시글 삭제 실패 (없는 경로)")
    @Test
    @WithAccount("ljseokd")
    void delete_notice_fail() throws Exception {
        assertThrows(Exception.class,
                () -> mockMvc.perform(post("/notice/123/delete")
                        .with(csrf()))) ;
    }

    @DisplayName("게시글 리스트")
    @Test
    void list_notice() throws Exception {
        mockMvc.perform(get("/notice/list"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("noticePage"))
                .andExpect(view().name("notice/list"))
                .andDo(print());
    }

}