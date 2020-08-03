package me.ljseokd.basicboard.controller;

import me.ljseokd.basicboard.MockMvcTest;
import me.ljseokd.basicboard.WithAccount;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@MockMvcTest
class AccountSettingControllerTest {

    @Autowired
    MockMvc mockMvc;

    @DisplayName("나의 계정 설정 메인 페이지")
    @Test
    @WithAccount("ljseokd")
    void settings_main_page() throws Exception {
        mockMvc.perform(get("/settings"))
                .andExpect(status().isOk())
                .andExpect(view().name("account/settings"))
                .andExpect(model().attributeExists("profileDto"))
                .andExpect(authenticated());
    }

}