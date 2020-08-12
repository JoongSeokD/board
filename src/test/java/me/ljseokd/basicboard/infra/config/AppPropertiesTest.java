package me.ljseokd.basicboard.infra.config;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("dev")
class AppPropertiesTest {

    @Autowired
    AppProperties appProperties;

    @DisplayName("yml파일 app.path 경로 확인")
    @Test
    void propertiesPath(){
        String path = appProperties.getPath();
        assertEquals("C:/board_atch", path);
    }
}