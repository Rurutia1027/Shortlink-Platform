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

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.test.autoconfigure.AutoConfigureMybatisPlus;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.ucd.shortlink.project.common.convention.result.Result;
import org.ucd.shortlink.project.dto.req.RecycleBinRecoverReqDTO;
import org.ucd.shortlink.project.dto.req.RecycleBinSaveReqDTO;
import org.ucd.shortlink.project.dto.resp.ShortLinkPageRespDTO;
import org.ucd.shortlink.project.service.RecycleBinService;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
    @SneakyThrows
    @DisplayName("Given valid request, when saveRecycleBin is called, then it returns success")
    public void givenValidRequest_whenSaveRecycleBin_thenReturnSuccess() {
        // --- Given ---
        String requestJson = """
                    {
                        "gid": "group123",
                        "fullShortUrl": "http://short.link/abc123"
                    }
                """;
        Mockito.doNothing().when(recycleBinService).saveRecycleBin(any(RecycleBinSaveReqDTO.class));

        // --- When & Then ---
        mockMvc.perform(post("/api/short-link/v1/recycle-bin/save")
                        .contentType("application/json")
                        .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(Result.SUCCESS_CODE))
                .andExpect(jsonPath("$.data").isEmpty())
                .andExpect(jsonPath("$.message").isEmpty());
    }

    @Test
    @SneakyThrows
    @DisplayName("Given valid request, when recoverRecycleBin is called, then it returns " +
            "success")
    public void givenValidRequest_whenRecoverRecycleBin_thenReturnSuccess() {
        // --- Given ---
        String requestJson = """
                {
                    "gid": "group123",
                    "fullShortUrl": "http://short.link/abc123"
                }
                """;

        // -- mock controller inner service --
        Mockito.doNothing().
                when(recycleBinService).recoverRecycleBin(any(RecycleBinRecoverReqDTO.class));


        // --- When & Then ---
        mockMvc.perform(post("/api/short-link/v1/recycle-bin/recover")
                        .contentType("application/json")
                        .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(Result.SUCCESS_CODE))
                .andExpect(jsonPath("$.data").isEmpty())
                .andExpect(jsonPath("$.message").isEmpty());
    }

    @Test
    @SneakyThrows
    @DisplayName("Given valid request, when pageShortLink is called, then it will returns " +
            "success")
    public void givenValidRequest_whenPageShortLink_thenReturnSuccess() {
        // --- Given ---
        String requestJson = """
                {
                  "current": 1,
                  "size": 10,
                  "gidList": ["gid123", "gid456", "gid789"]
                }
                """;
        // -- mock controller inner service --
        IPage<ShortLinkPageRespDTO> pageResp = new Page<>();
        pageResp.setSize(1L).setCurrent(1L).setPages(1L).setTotal(10L)
                .setRecords(List.of(ShortLinkPageRespDTO
                        .builder()
                        .createTime(new Date())
                        .describe(UUID.randomUUID().toString())
                        .fullShortUrl("https://baidu.com")
                        .originUrl("https://baidu.com")
                        .gid(UUID.randomUUID().toString())
                        .todayPv(1)
                        .todayUIp(1)
                        .domain(UUID.randomUUID().toString())
                        .shortUri(UUID.randomUUID().toString())
                        .totalUv(1)
                        .favicon(UUID.randomUUID().toString())
                        .build()));

        when(recycleBinService.pageShortLink(any())).thenReturn(pageResp);


        // --- When & Then ---
        mockMvc.perform(post("/api/short-link/v1/recycle-bin/page")
                        .contentType("application/json")
                        .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(Result.SUCCESS_CODE))
                .andExpect(jsonPath("$.data").isNotEmpty())
                .andExpect(jsonPath("$.data.records[0].fullShortUrl").value("https://baidu.com"))
                .andExpect(jsonPath("$.data.records[0].originUrl").value("https://baidu.com"))
                .andExpect(jsonPath("$.message").isEmpty());
    }

    @Test
    @SneakyThrows
    @DisplayName("Given valid request, when removeRecycleBin is called, then it will returns " +
            "success")
    public void givenValidRequest_whenRemoveRecycleBin_thenReturnSuccess() {
        // --- Given ---
        String requestJson = """
                {
                }
                """;

        // -- mock controller inner service --
        doNothing().when(recycleBinService).removeRecycleBin(any());


        // --- When & Then ---
        mockMvc.perform(post("/api/short-link/v1/recycle-bin/remove")
                        .contentType("application/json")
                        .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(Result.SUCCESS_CODE))
                .andExpect(jsonPath("$.data").isEmpty())
                .andExpect(jsonPath("$.message").isEmpty());
    }
}