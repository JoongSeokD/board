package me.ljseokd.basicboard.controller;

import me.ljseokd.basicboard.domain.Account;
import me.ljseokd.basicboard.repository.AccountRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class AccountControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    AccountRepository accountRepository;

    @DisplayName("로그인 페이지")
    @Test
    void login() throws Exception {
        mockMvc
                .perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"))
                .andExpect(unauthenticated());
    }

    @DisplayName("회원 가입 페이지")
    @Test
    void sign_up_form() throws Exception {
        mockMvc.perform(get("/sign-up"))
                .andExpect(status().isOk())
                .andExpect(view().name("sign-up"))
                .andExpect(unauthenticated());
    }

    @DisplayName("회원 가입 성공")
    @Test
    void sign_up_success() throws Exception {
        mockMvc.perform(post("/sign-up")
                .param("username", "ljseokd")
                .param("password", "12345678")
                .with(csrf()))
                .andDo(print())
                .andExpect(model().hasNoErrors())
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/"))
                .andExpect(authenticated())
        ;

        Account ljseokd = accountRepository.findByName("ljseokd");
        Assertions.assertNotEquals("12345678",ljseokd.getPassword());
    }
    
    @DisplayName("회원가입 실패 (중복된 이름)")
    @Test
    void sign_up_duplicate_fail() throws Exception {
        accountRepository.save(new Account("ljseokd", "12345678"));

        mockMvc.perform(post("/sign-up")
                .param("username", "ljseokd")
                .param("password", "12345678")
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(model().hasErrors())
                .andExpect(view().name("sign-up"))
                .andExpect(unauthenticated());
    }
    @DisplayName("회원가입 실패 (비밀번호 길이 부족)")
    @Test
    void sign_up_password_length_fail() throws Exception {
        mockMvc.perform(post("/sign-up")
                .param("username", "ljseokd")
                .param("password", "1234567")
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(model().hasErrors())
                .andExpect(view().name("sign-up"))
                .andExpect(unauthenticated());
    }

}
