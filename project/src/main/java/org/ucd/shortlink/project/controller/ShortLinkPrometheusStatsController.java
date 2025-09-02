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
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.ucd.shortlink.project.common.convention.result.Result;
import org.ucd.shortlink.project.common.convention.result.Results;
import org.ucd.shortlink.project.dto.req.metrics.ShortLinkGroupStatsAccessRecordReqDTO;
import org.ucd.shortlink.project.dto.req.metrics.ShortLinkStatsReqDTO;
import org.ucd.shortlink.project.dto.resp.metrics.ShortLinkStatsAccessRecordRespDTO;
import org.ucd.shortlink.project.dto.resp.metrics.ShortLinkStatsRespDTO;
import org.ucd.shortlink.project.service.ShortLinkStatsService;

import java.util.UUID;

/**
 * Short link monitor metrics controller
 */
@RestController
@RequiredArgsConstructor
public class ShortLinkPrometheusStatsController {
    @Qualifier(value = "PrometheusStatsService")
    private final ShortLinkStatsService shortLinkStatsService;


    @GetMapping("/api/short-link/v1/prometheus/hello")
    public String hello() {
        return "Hello Metrics!";
    }

    @SneakyThrows
    @GetMapping("/api/short-link/v1/prometheus/metrics")
    public String metrics() {
        return UUID.randomUUID().toString();
    }

    /**
     * Short link item monitor metrics
     */
    @GetMapping("/api/short-link/v1/prometheus/stats")
    public Result<ShortLinkStatsRespDTO> shortLinkStats(ShortLinkStatsReqDTO requestParams) {
        return Results.success(shortLinkStatsService.oneShortLinkStats(requestParams));
    }

    /**
     * Fetch grouping short link monitor metrics
     */
    @GetMapping("/api/short-link/v1/stats/prometheus/access-record/group")
    public Result<IPage<ShortLinkStatsAccessRecordRespDTO>> groupShortLinkStatsAccessRecord(ShortLinkGroupStatsAccessRecordReqDTO requestParam) {
        return Results.success(shortLinkStatsService.groupShortLinkStatsAccessRecord(requestParam));
    }
}
