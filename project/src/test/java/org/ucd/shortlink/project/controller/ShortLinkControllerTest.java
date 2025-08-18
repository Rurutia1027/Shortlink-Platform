package org.ucd.shortlink.project.controller;

import com.baomidou.mybatisplus.test.autoconfigure.AutoConfigureMybatisPlus;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.ucd.shortlink.project.common.convention.result.Result;
import org.ucd.shortlink.project.dto.resp.ShortLinkCreateRespDTO;
import org.ucd.shortlink.project.service.ShortLinkService;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ShortLinkController.class)
@AutoConfigureMybatisPlus
class ShortLinkControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ShortLinkService shortLinkService;

    @Test
    @DisplayName("Validate Mock Init OK")
    public void testMockInitOK() {
        Assertions.assertNotNull(mockMvc);
        Assertions.assertNotNull(shortLinkService);
    }

    @Test
    @SneakyThrows
    @DisplayName("Given valid request, when restoreUrl is called, then it returns success")
    public void givenValidRequest_whenRestoreUrl_thenReturnSuccess() {
        // --- Given ---
        String shortUri = "abc123";
        String originUrl = "https://baidu.com";

        // -- mock controller internal service behaviors
        doAnswer(invocation -> {
            HttpServletResponse resp = invocation.getArgument(2);
            resp.sendRedirect(originUrl);
            ;
            return null;
        }).when(shortLinkService).restoreUrl(eq(shortUri), any(HttpServletRequest.class),
                any(HttpServletResponse.class));

        // --- When & Then ---
        mockMvc.perform(get("/" + shortUri))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(originUrl));
    }

    @Test
    @SneakyThrows
    @DisplayName("Given valid request, when createShortLink is called, then it returns success")
    public void givenValidRequest_createShortLink_thenReturnSuccess() {
        // --- Given ---
        String requestJson = """
                {
                    "domain": "short.example.com",
                    "originUrl": "https://baidu.com",
                    "gid": "group-123",
                    "createdType": 0,
                    "validDateType": 1,
                    "validDate": "2025-08-20 12:00:00",
                    "describe": "Test short link creation"
                }
                """;

        // -- mock inner service logic --
        when(shortLinkService.createShortLink(any())).thenReturn(ShortLinkCreateRespDTO.builder()
                .gid(UUID.randomUUID().toString())
                .fullShortUrl(UUID.randomUUID().toString())
                .originalUrl(UUID.randomUUID().toString())
                .build());

        // --- When && Then --
        mockMvc.perform(post("/api/short-link/v1/create")
                .contentType("application/json")
                .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(Result.SUCCESS_CODE))
                .andExpect(jsonPath("$.data").isNotEmpty())
                .andExpect(jsonPath("$.message").isEmpty());
    }
}