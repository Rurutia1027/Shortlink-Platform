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