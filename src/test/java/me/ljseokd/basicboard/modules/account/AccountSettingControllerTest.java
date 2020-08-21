package me.ljseokd.basicboard.modules.account;

import me.ljseokd.basicboard.infra.MockMvcTest;
import me.ljseokd.basicboard.modules.account.form.SignUpForm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@MockMvcTest
class AccountSettingControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    AccountService accountService;

    @Autowired
    AccountRepository accountRepository;

    @BeforeEach
    void beForeEach(){
        SignUpForm signUpForm = new SignUpForm();
        signUpForm.setUsername("ljseokd");
        signUpForm.setPassword("12341234");
        accountService.join(signUpForm);
    }

    @DisplayName("내 소개 설정 페이지")
    @Test
    void descriptionForm() throws Exception {
        mockMvc.perform(get("/settings/description"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("profileForm"))
                .andExpect(view().name("account/settings/description"));
    }

    @Test
    @DisplayName("내 소개 설정 성공")
    void descriptionSubmit_success() throws Exception {
        String username = "ljseokd";
        Account account = accountRepository.findByNickname(username).get();
        String findBio = account.getBio();
        String setBio = "안녕하세요 잘 부탁드립니다.";

        mockMvc.perform(post("/settings/description")
                .param("bio", setBio)
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(model().hasNoErrors())
                .andExpect(view().name("redirect:/profile/" + username));

        assertNull(findBio);
        assertNotEquals(findBio, setBio);
    }

    @Test
    @DisplayName("내 소개 설정 실패 (bio 길이 초과)")
    void descriptionSubmit_fail() throws Exception {
        Account account = accountRepository.findByNickname("ljseokd").get();
        String findBio = account.getBio();
        String setBio = "안녕하세요 잘 부탁드립니다.잘 부탁드립니다.잘 부탁드립니다.잘 부탁드립니다.잘 부탁드립니다.잘 부탁드립니다.잘 부탁드립니다.잘 부탁드립니다.잘 부탁드립니다.잘 부탁드립니다.잘 부탁드립니다.잘 부탁드립니다.";

        mockMvc.perform(post("/settings/description")
                .param("bio", setBio)
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(model().hasErrors())
                .andExpect(model().attributeExists("profileForm"))
                .andExpect(view().name("account/settings/description"));

        assertNull(findBio);
        assertEquals(findBio, account.getBio());
    }

}