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

package org.ucd.shortlink.admin.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.ucd.shortlink.admin.common.convention.result.Result;
import org.ucd.shortlink.admin.dto.req.ShortLinkStatsAccessRecordReqDTO;
import org.ucd.shortlink.admin.remote.ShortLinkRemoteProjectService;
import org.ucd.shortlink.admin.remote.dto.req.ShortLinkGroupStatsAccessRecordReqDTO;
import org.ucd.shortlink.admin.remote.dto.req.ShortLinkStatsReqDTO;
import org.ucd.shortlink.admin.remote.dto.resp.ShortLinkStatsAccessRecordRespDTO;
import org.ucd.shortlink.admin.remote.dto.resp.ShortLinkStatsRespDTO;

/**
 * Short Link Admin side Monitor State Controller
 */

@RestController(value = "shortLinkStatsControllerByAdmin")
@RequiredArgsConstructor
public class ShortLinkStatsController {
    private final ShortLinkRemoteProjectService shortLinkRemoteProjectService;

    /**
     * Short link monitor metric stats
     */
    @GetMapping("/api/short-link/admin/v1/stats")
    public Result<ShortLinkStatsRespDTO> shortLinkStats(ShortLinkStatsReqDTO requestParam) {
        return shortLinkRemoteProjectService.shortLinkStats(requestParam);
    }

    /**
     * Fetch project side's short link monitor stats
     */
    @GetMapping("/api/short-link/admin/v1/stats/access-record")
    public Result<Page<ShortLinkStatsAccessRecordRespDTO>> shortLinkStatsAccessRecord(ShortLinkStatsAccessRecordReqDTO requestParam) {
        return shortLinkRemoteProjectService.shortLinkStatsAccessRecords(requestParam);
    }


    /**
     * Fetch short link grouped monitor statistic records
     */
    @GetMapping("/api/short-link/admin/v1/stats/access-record/group")
    public Result<Page<ShortLinkStatsAccessRecordRespDTO>> groupShortLinkStatsAccessRecord(ShortLinkGroupStatsAccessRecordReqDTO requestParam) {
        return shortLinkRemoteProjectService.groupShortLinkStatsAccessRecord(requestParam);
    }
}
