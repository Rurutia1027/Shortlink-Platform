package org.ucd.shortlink.project.controller;

import com.baomidou.mybatisplus.test.autoconfigure.AutoConfigureMybatisPlus;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(controllers = ShortLinkNotfoundController.class)
@AutoConfigureMybatisPlus
class ShortLinkNotfoundControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Validate Mock Init OK")
    public void testMockInitOK() {
        Assertions.assertNotNull(mockMvc);
    }

    @Test
    @SneakyThrows
    @DisplayName("Given Valid request send request to not found endpoint, should return not " +
            "found and success status")
    public void givenValidRequest_whenNotFound_thenReturnNotFoundStr() {
        mockMvc.perform(get("/page/notfound"))
                .andExpect(status().isOk())
                .andExpect(view().name("notfound"));
    }
}