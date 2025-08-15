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
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.ucd.shortlink.admin.common.convention.result.Result;
import org.ucd.shortlink.admin.common.convention.result.Results;
import org.ucd.shortlink.admin.dto.resp.ShortLinkGroupCountQueryRespDTO;
import org.ucd.shortlink.admin.remote.ShortLinkRemoteProjectService;
import org.ucd.shortlink.admin.remote.dto.req.RecycleBinRemoveReqDTO;
import org.ucd.shortlink.admin.remote.dto.req.ShortLinkBatchCreateReqDTO;
import org.ucd.shortlink.admin.remote.dto.req.ShortLinkCreateReqDTO;
import org.ucd.shortlink.admin.remote.dto.req.ShortLinkPageReqDTO;
import org.ucd.shortlink.admin.remote.dto.req.ShortLinkUpdateReqDTO;
import org.ucd.shortlink.admin.remote.dto.resp.ShortLinkBaseInfoRespDTO;
import org.ucd.shortlink.admin.remote.dto.resp.ShortLinkBatchCreateRespDTO;
import org.ucd.shortlink.admin.remote.dto.resp.ShortLinkCreateRespDTO;
import org.ucd.shortlink.admin.remote.dto.resp.ShortLinkPageRespDTO;
import org.ucd.shortlink.admin.toolkit.EasyExcelWebUtil;

import java.util.List;

@RestController(value = "shortLinkControllerByAdmin")
@RequiredArgsConstructor
public class ShortLinkController {

    private final ShortLinkRemoteProjectService shortLinkRemoteProjectService;

    /**
     * Create short link
     */
    @PostMapping("/api/short-link/admin/v1/create")
    public Result<ShortLinkCreateRespDTO> createShortLink(@RequestBody ShortLinkCreateReqDTO requestParam) {
        return shortLinkRemoteProjectService.createShortLink(requestParam);
    }

    /**
     * Create short link in Bulk
     */
    @SneakyThrows
    @PostMapping("/api/short-link/admin/v1/create/batch")
    public void batchCreateShortLink(@RequestBody ShortLinkBatchCreateReqDTO requestParam, HttpServletResponse response) {
        Result<ShortLinkBatchCreateRespDTO> shortLinkBatchCreateRespDTOResult =
                shortLinkRemoteProjectService.batchCreateShortLink(requestParam);
        if (shortLinkBatchCreateRespDTOResult.isSuccess()) {
            List<ShortLinkBaseInfoRespDTO> baseLinkInfos = shortLinkBatchCreateRespDTOResult.getData().getBaseLinkInfos();
            EasyExcelWebUtil.write(response, "Batch_ShortLink-SaaS_ShortLink_Platform",
                    ShortLinkBaseInfoRespDTO.class, baseLinkInfos);
        }
    }

    /**
     * Update Short Link
     */
    @PutMapping("/api/short-link/admin/v1/update")
    public Result<Void> updateShortLink(@RequestBody ShortLinkUpdateReqDTO requestParam) {
        shortLinkRemoteProjectService.updateShortLink(requestParam);
        return Results.success();
    }


    /**
     * Paging query short link
     */
    @PostMapping("/api/short-link/admin/v1/page")
    public Result<Page<ShortLinkPageRespDTO>> pageShortLink(@RequestBody ShortLinkPageReqDTO requestParam) {
        return shortLinkRemoteProjectService.pageShortLink(requestParam);
    }

    /**
     * Query short link count per group
     */
    @GetMapping("/api/short-link/admin/v1/count")
    public Result<List<ShortLinkGroupCountQueryRespDTO>> listGroupShortLinkCount(@RequestParam List<String> requestParam) {
        return shortLinkRemoteProjectService.listGroupShortLinkCount(requestParam);
    }

    /**
     * Remove recycled Short Link Item
     */
    @PostMapping("/api/short-link/admin/v1/recycle-bin/remove")
    public Result<Void> removeRecycleBin(@RequestBody RecycleBinRemoveReqDTO requestParam) {
        shortLinkRemoteProjectService.removeRecycleBin(requestParam);
        return Results.success();
    }
}
