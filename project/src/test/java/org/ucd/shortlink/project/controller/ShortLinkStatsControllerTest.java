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

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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
import org.ucd.shortlink.project.dto.resp.metrics.ShortLinkStatsAccessRecordRespDTO;
import org.ucd.shortlink.project.dto.resp.metrics.ShortLinkStatsRespDTO;
import org.ucd.shortlink.project.service.ShortLinkStatsService;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ShortLinkStatsController.class)
@AutoConfigureMybatisPlus
class ShortLinkStatsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ShortLinkStatsService shortLinkStatsService;

    @Test
    @DisplayName("Validate Mock Init OK")
    public void testMockInitOK() {
        Assertions.assertNotNull(mockMvc);
        Assertions.assertNotNull(shortLinkStatsService);
    }

    @Test
    @SneakyThrows
    @DisplayName("Given valid request to shortLinkStats, will return success response")
    public void givenValidRequest_whenShortLinkStats_thenReturnSuccessResponse() {
        // --- Given ---
        String fullShortLink = UUID.randomUUID().toString();
        String gid = UUID.randomUUID().toString();
        String startDate = DateUtil.formatDate(new Date());
        String endDate = DateUtil.formatDate(new Date());

        // -- mock inner service action --
        when(shortLinkStatsService.oneShortLinkStats(any()))
                .thenReturn(ShortLinkStatsRespDTO.builder().build());

        // --- When && Then --
        mockMvc.perform(get("/api/short-link/v1/stats")
                        .param("fullShortLink", fullShortLink)
                        .param("gid", gid)
                        .param("startDate", startDate)
                        .param("endDate", endDate))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(Result.SUCCESS_CODE))
                .andExpect(jsonPath("$.data").isNotEmpty());
    }

    @Test
    @SneakyThrows
    @DisplayName("Given valid request to groupShortLinkStatsAccessRecord, will return success response")
    public void givenValidRequest_whenGroupShortLinkStatsAccessRecord_thenReturnSuccessResponse() {
        // --- Given ---
        String gid = UUID.randomUUID().toString();
        String startDate = DateUtil.formatDate(new Date());
        String endDate = DateUtil.formatDate(new Date());
        String current = "1";
        String size = "1";

        // -- mock inner service action ---
        IPage<ShortLinkStatsAccessRecordRespDTO> respDTOIPage = new Page<>();
        respDTOIPage.setRecords(List.of(ShortLinkStatsAccessRecordRespDTO.builder().build()))
                .setCurrent(1)
                .setTotal(10)
                .setSize(3)
                .setPages(1);
        when(shortLinkStatsService.groupShortLinkStatsAccessRecord(any()))
                .thenReturn(respDTOIPage);

        // --- When && Then ---
        mockMvc.perform(get("/api/short-link/v1/stats/access-record/group")
                        .param("gid", gid)
                        .param("startDate", startDate)
                        .param("endDate", endDate)
                        .param("current", current)
                        .param("size", size))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(Result.SUCCESS_CODE))
                .andExpect(jsonPath("$.data").isNotEmpty());
    }
}