/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.ucd.shortlink.project.controller;

import com.baomidou.mybatisplus.test.autoconfigure.AutoConfigureMybatisPlus;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.ucd.shortlink.project.common.convention.result.Result;
import org.ucd.shortlink.project.service.UrlTitleService;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UrlTitleController.class)
@AutoConfigureMybatisPlus
class UrlTitleControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UrlTitleService urlTitleService;

    @Test
    @DisplayName("Validate mock init OK")
    public void initOk() {
        Assertions.assertNotNull(mockMvc);
        Assertions.assertNotNull(urlTitleService);
    }

    @Test
    @SneakyThrows
    @DisplayName("Given validate request when invoke getTitleByUrl then success response " +
            "should be returned")
    public void givenValidRequest_whenGetTitleByUrl_thenSuccessResponseReturn() {
        // --- Given ---
        String url = "https://baidu.com";

        // -- mock inner service behave --
        when(urlTitleService.getTitleByUrl(anyString())).thenReturn("Baidu Welcome");

        // --- When && Then ---
        mockMvc.perform(get("/api/short-link/v1/title")
                .param("url", url))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(Result.SUCCESS_CODE))
                .andExpect(jsonPath("$.data").isNotEmpty());
    }
}