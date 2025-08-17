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
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.ucd.shortlink.project.common.convention.result.Result;
import org.ucd.shortlink.project.dto.req.RecycleBinSaveReqDTO;
import org.ucd.shortlink.project.service.RecycleBinService;

import static org.mockito.ArgumentMatchers.any;

@WebMvcTest(controllers = RecycleBinController.class)
@AutoConfigureMybatisPlus
public class RecycleBinControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RecycleBinService recycleBinService;

    @Test
    @DisplayName("Validate Mock Init OK")
    public void testGetEmptyRecycleBin() {
        Assertions.assertNotNull(mockMvc);
        Assertions.assertNotNull(recycleBinService);
    }

    @Test
    @DisplayName("Given valid request, when saveRecycleBin is called, then it returns success")
    public void givenValidRequest_whenSaveRecycleBin_thenReturnSuccess() throws Exception {
        // --- Given ---
        String requestJson = """
                    {
                        "gid": "group123",
                        "fullShortUrl": "http://short.link/abc123"
                    }
                """;
        Mockito.doNothing().when(recycleBinService).saveRecycleBin(any(RecycleBinSaveReqDTO.class));

        // --- When & Then ---
        mockMvc.perform(MockMvcRequestBuilders.post("/api/short-link/v1/recycle-bin/save")
                        .contentType("application/json")
                        .content(requestJson))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(Result.SUCCESS_CODE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").isEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").isEmpty());
    }

}