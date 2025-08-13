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

package org.ucd.shortlink.admin.remote;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.ucd.shortlink.admin.common.convention.result.Result;
import org.ucd.shortlink.admin.dto.resp.ShortLinkGroupCountQueryRespDTO;
import org.ucd.shortlink.admin.remote.dto.req.RecycleBinRecoverReqDTO;
import org.ucd.shortlink.admin.remote.dto.req.RecycleBinRemoveReqDTO;
import org.ucd.shortlink.admin.remote.dto.req.RecycleBinSaveReqDTO;
import org.ucd.shortlink.admin.remote.dto.req.ShortLinkBatchCreateReqDTO;
import org.ucd.shortlink.admin.remote.dto.req.ShortLinkCreateReqDTO;
import org.ucd.shortlink.admin.remote.dto.req.ShortLinkUpdateReqDTO;
import org.ucd.shortlink.admin.remote.dto.resp.ShortLinkBatchCreateRespDTO;
import org.ucd.shortlink.admin.remote.dto.resp.ShortLinkCreateRespDTO;
import org.ucd.shortlink.admin.remote.dto.resp.ShortLinkPageRespDTO;
import org.ucd.shortlink.admin.remote.dto.resp.ShortLinkStatsAccessRecordRespDTO;
import org.ucd.shortlink.admin.remote.dto.resp.ShortLinkStatsRespDTO;

import java.util.List;

/**
 * Short link remote project service Stub
 */

@FeignClient("short-link-project")
public interface ShortLinkProjectService {
    /**
     * Create short link
     *
     * @param requestParam short link create request params
     * @return short link creation response body
     */
    @PostMapping("/api/short-link/v1/create")
    Result<ShortLinkCreateRespDTO> createShortLink(@RequestBody ShortLinkCreateReqDTO requestParam);


    /**
     * Batch create short links
     *
     * @param requestParam batch create short links request params
     * @return batch creation response body
     */
    @PostMapping("/api/short-link/v1/create/batch")
    Result<ShortLinkBatchCreateRespDTO> batchCreateShortLink(@RequestBody ShortLinkBatchCreateReqDTO requestParam);

    /**
     * Short link update
     *
     * @param requestParam short link update request params
     */
    @PostMapping("/api/short-link/v1/update")
    void updateShortLink(@RequestBody ShortLinkUpdateReqDTO requestParam);

    /**
     * Paging query short link
     *
     * @param gid      Group ID
     * @param orderTag sorting tag
     * @param current  current page
     * @param size     page item size
     * @return paging query short link response body
     */
    @GetMapping("/api/short-link/v1/page")
    Result<Page<ShortLinkPageRespDTO>> pageShortLink(@RequestParam("gid") String gid,
                                                     @RequestParam("orderTag") String orderTag,
                                                     @RequestParam("current") Long current,
                                                     @RequestParam("size") Long size);

    /**
     * Query short link count in the given group
     *
     * @param requestParam grouped short link query cnt request param
     * @return grouped short link count value
     */
    @GetMapping("/api/short-link/v1/count")
    Result<List<ShortLinkGroupCountQueryRespDTO>> listGroupShortLinkCount(@RequestParam(
            "requestParam") List<String> requestParam);


    /**
     * Fetch Website Title via given URL
     *
     * @param url website url address
     * @return website title
     */
    @GetMapping("/api/short-link/v1/title")
    Result<String> getTitleByUrl(@RequestParam("url") String url);


    /**
     * Preserve short link to Recycle Bin
     *
     * @param requestParam request params
     */
    @PostMapping("/api/short-link/v1/recycle-bin/save")
    void saveRecycleBin(@RequestBody RecycleBinSaveReqDTO requestParam);


    /**
     * Paging query recycled short link items
     *
     * @param gidList group ID collection
     * @param current current page
     * @param size    total items in current page
     * @return paged short link items
     */
    @GetMapping("/api/short-link/v1/recycle-bin/page")
    Result<Page<ShortLinkPageRespDTO>> pageRecycleBinShortLink(@RequestParam("gidList") List<String> gidList,
                                                               @RequestParam("current") Long current,
                                                               @RequestParam("size") Long size);

    /**
     * Recover recycled short link from Recycle Bin
     *
     * @param requestParam request params for recover short link for recycled to be available
     */
    @PostMapping("/api/short-link/v1/recycle-bin/recover")
    void recoverRecycleBin(@RequestBody RecycleBinRecoverReqDTO requestParam);

    /**
     * Remove short link
     *
     * @param requestParam short link delete request param
     */
    @PostMapping("/api/short-link/v1/recycle-bin/remove")
    void removeRecycleBin(@RequestBody RecycleBinRemoveReqDTO requestParam);


    /**
     * Fetch given short link monitor metric records in given specified date range.
     * <p>
     * TODO: preserve API but modify underlying metric collection & storage strategy here.
     *
     * @param fullShortUrl full short url address
     * @param gid          group ID
     * @param startDate    start date monitor metric query
     * @param endDate      end date for monitoring metric query
     * @return queried short link monitor metric records
     */
    @GetMapping("/api/short-link/v1/stats")
    Result<ShortLinkStatsRespDTO> shortLinkStats(@RequestParam("fullShortUrl") String fullShortUrl,
                                                 @RequestParam("gid") String gid,
                                                 @RequestParam("startDate") String startDate,
                                                 @RequestParam("endDate") String endDate);


    /**
     * Fetch short links in the given group monitor metric records in the given specific
     * date range.
     *
     * @param gid       group ID
     * @param startDate start date
     * @param endDate   end date
     * @return short link monitor metrics in given group and in specific date range
     */
    @GetMapping("/api/short-link/v1/stats/group")
    Result<ShortLinkStatsRespDTO> groupShortLinkStats(@RequestParam("gid") String gid,
                                                      @RequestParam("startDate") String startDate,
                                                      @RequestParam("endDate") String endDate);

    /**
     * Fetch short link in specific group and specific date ranges access monitor metrics
     *
     * @param fullShortUrl short link url
     * @param gid          group ID that queried short link locates
     * @param startDate    query monitor metrics start date
     * @param endDate      query monitor metrics end date
     * @return queried monitor metric records
     */
    @GetMapping("/api/short-link/v1/stats/access-records")
    Result<Page<ShortLinkStatsAccessRecordRespDTO>> shortLinkStatsAccessRecords(@RequestParam("fullShortUrl") String fullShortUrl,
                                                                                @RequestParam("gid") String gid,
                                                                                @RequestParam("startDate") String startDate,
                                                                                @RequestParam("endDate") String endDate);

    /**
     * Fetch monitor metric records of short links in the given specific group ID.
     *
     * @param gid       group ID
     * @param startDate start date of querying monitor metrics
     * @param endDate   end date of querying monitor metrics
     * @return short link access monitor metrics in the given group ID and given specific
     * date range
     */
    @GetMapping("/api/short-link/v1/stats/access-record/group")
    Result<Page<ShortLinkStatsAccessRecordRespDTO>> groupShortLinkStatsAccessRecord(@RequestParam("gid") String gid,
                                                                                    @RequestParam("startDate") String startDate,
                                                                                    @RequestParam("endDate") String endDate);
}
