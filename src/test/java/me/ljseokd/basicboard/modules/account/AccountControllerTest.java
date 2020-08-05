package me.ljseokd.basicboard.modules.account;

import me.ljseokd.basicboard.infra.MockMvcTest;
import me.ljseokd.basicboard.modules.main.form.SignUpForm;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@MockMvcTest
class AccountControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    AccountService accountService;


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