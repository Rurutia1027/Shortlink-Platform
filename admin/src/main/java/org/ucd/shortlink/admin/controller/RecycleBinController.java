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

import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.ucd.shortlink.admin.common.convention.result.Result;
import org.ucd.shortlink.admin.common.convention.result.Results;
import org.ucd.shortlink.admin.remote.ShortLinkRemoteService;
import org.ucd.shortlink.admin.remote.dto.req.RecycleBinRecoverReqDTO;
import org.ucd.shortlink.admin.remote.dto.req.RecycleBinSaveReqDTO;
import org.ucd.shortlink.admin.remote.dto.req.ShortLinkPageReqDTO;
import org.ucd.shortlink.admin.remote.dto.req.ShortLinkRecycleBinPageReqDTO;
import org.ucd.shortlink.admin.remote.dto.resp.ShortLinkPageRespDTO;
import org.ucd.shortlink.admin.service.RecycleBinService;

/**
 * Recycle Bin Controller layer
 */

@RestController
@RequiredArgsConstructor
public class RecycleBinController {

    private final RecycleBinService recycleBinService;

    // TODO: Refactor into SpringCloud Feign invocation
    ShortLinkRemoteService shortLinkRemoteService = new ShortLinkRemoteService() {
    };

    /**
     * Move short link to recycle bin
     */
    @PostMapping("/api/short-link/admin/v1/recycle-bin/save")
    public Result<Void> saveRecycleBin(@RequestBody RecycleBinSaveReqDTO recycleBinSaveReqDTO) {
        shortLinkRemoteService.saveRecycleBin(recycleBinSaveReqDTO);
        return Results.success();
    }

    /**
     * Paging query short link
     */
    @PostMapping("/api/short-link/admin/v1/recycle-bin/page")
    public Result<IPage<ShortLinkPageRespDTO>> pageShortLink(@RequestBody ShortLinkRecycleBinPageReqDTO requestParam) {
        return recycleBinService.pageRecycledShortLink(requestParam);
    }

    /**
     * Recover recycled Short Link Item back to normal Short Link.
     */
    @PostMapping("/api/short-link/admin/v1/recycle-bin/recover")
    public Result<Void> recoverRecycleBin(@RequestBody RecycleBinRecoverReqDTO requestParam) {
        shortLinkRemoteService.recoverRecycleBin(requestParam);
        return Results.success();
    }
}
