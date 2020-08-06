package me.ljseokd.basicboard.modules.account;

import me.ljseokd.basicboard.infra.MockMvcTest;
import me.ljseokd.basicboard.modules.main.form.SignUpForm;
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
class AccountControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    AccountService accountService;

    @Autowired
    AccountRepository accountRepository;


    @DisplayName("회원 가입 페이지")
    @Test
    void sign_up_form() throws Exception {
        mockMvc.perform(get("/sign-up"))
                .andExpect(status().isOk())
                .andExpect(view().name("sign-up"))
                .andDo(print())
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


    @Test
    @DisplayName("프로필 보기 성공")
    void view_profile_success() throws Exception {
        // given
        SignUpForm signUpForm = new SignUpForm();
        String username = "홍길동";
        signUpForm.setUsername(username);
        signUpForm.setPassword("12341234");
        accountService.join(signUpForm);
        // when
        mockMvc.perform(get("/profile/"+ username))
                .andExpect(status().isOk())
                .andExpect(model().attribute("isOwner", true))
                .andExpect(view().name("account/profile"))
                .andDo(print());
    }

    @Test
    @DisplayName("다른 사용자의 프로필 접근")
    void view_profile_non_owner() throws Exception {
        // given
        SignUpForm signUpForm = new SignUpForm();
        String username = "홍길동";
        signUpForm.setUsername(username);
        signUpForm.setPassword("12341234");
        accountService.join(signUpForm);
        SignUpForm signUpForm2 = new SignUpForm();
        String username2 = "홍길동2";
        signUpForm2.setUsername(username2);
        signUpForm2.setPassword("123412342");
        accountService.join(signUpForm2);
        // when
        mockMvc.perform(get("/profile/"+ username))
                .andExpect(status().isOk())
                .andExpect(model().attribute("isOwner", false))
                .andExpect(view().name("account/profile"))
                .andDo(print());
    }

    @Test
    @DisplayName("프로필 보기 잘못된 경로")
    void view_profile_fail() throws Exception {
        Assertions.assertThrows(Exception.class , () -> mockMvc.perform(get("/profile/홍길동")));
    }

}