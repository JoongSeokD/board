package me.ljseokd.basicboard.modules.main;

import me.ljseokd.basicboard.infra.AbstractContainerBaseTest;
import me.ljseokd.basicboard.infra.MockMvcTest;
import me.ljseokd.basicboard.modules.account.Account;
import me.ljseokd.basicboard.modules.account.AccountRepository;
import me.ljseokd.basicboard.modules.account.WithAccount;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@MockMvcTest
class MainControllerTest extends AbstractContainerBaseTest {

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

        Account ljseokd = accountRepository.findByNickname("ljseokd").get();
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

    @DisplayName("로그아웃")
    @Test
    @WithAccount("ljseokd")
    void logout() throws Exception {
        mockMvc.perform(post("/logout")
                .with(csrf()))
            .andExpect(status().is3xxRedirection())
            .andExpect(unauthenticated())
        ;

    }

}
