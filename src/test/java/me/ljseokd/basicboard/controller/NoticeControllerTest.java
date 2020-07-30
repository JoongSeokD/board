package me.ljseokd.basicboard.controller;

import me.ljseokd.basicboard.WithAccount;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import javax.transaction.Transactional;

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

}