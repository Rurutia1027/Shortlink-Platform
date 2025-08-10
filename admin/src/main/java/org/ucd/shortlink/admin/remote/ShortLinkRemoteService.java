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

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.TypeReference;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.web.bind.annotation.RequestBody;
import org.ucd.shortlink.admin.common.convention.result.Result;
import org.ucd.shortlink.admin.dto.req.ShortLinkStatsAccessRecordReqDTO;
import org.ucd.shortlink.admin.remote.dto.req.ShortLinkStatsReqDTO;
import org.ucd.shortlink.admin.dto.resp.ShortLinkGroupCountQueryRespDTO;
import org.ucd.shortlink.admin.remote.dto.resp.ShortLinkStatsAccessRecordRespDTO;
import org.ucd.shortlink.admin.remote.dto.resp.ShortLinkStatsRespDTO;
import org.ucd.shortlink.admin.remote.dto.req.RecycleBinRecoverReqDTO;
import org.ucd.shortlink.admin.remote.dto.req.RecycleBinRemoveReqDTO;
import org.ucd.shortlink.admin.remote.dto.req.RecycleBinSaveReqDTO;
import org.ucd.shortlink.admin.remote.dto.req.ShortLinkCreateReqDTO;
import org.ucd.shortlink.admin.remote.dto.req.ShortLinkPageReqDTO;
import org.ucd.shortlink.admin.remote.dto.req.ShortLinkRecycleBinPageReqDTO;
import org.ucd.shortlink.admin.remote.dto.req.ShortLinkUpdateReqDTO;
import org.ucd.shortlink.admin.remote.dto.resp.ShortLinkCreateRespDTO;
import org.ucd.shortlink.admin.remote.dto.resp.ShortLinkPageRespDTO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Short link endpoint remote call service
 */
public interface ShortLinkRemoteService {

    /**
     * Create Short Link Request
     *
     * @param requestParam create short link request param
     * @return short link creation response body
     */
    default Result<ShortLinkCreateRespDTO> createShortLink(ShortLinkCreateReqDTO requestParam) {
        String resultBodyStr = HttpUtil.post("http://127.0.0.1:8001/api/short-link/v1/create", JSON.toJSONString(requestParam));
        return JSON.parseObject(resultBodyStr, new TypeReference<>() {
        });
    }


    /**
     * Short link paging query
     *
     * @param requestParam paging request param
     * @return short link paging query response body
     */
    default Result<IPage<ShortLinkPageRespDTO>> pageShortLink(@RequestBody ShortLinkPageReqDTO requestParam) {
        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put("gid", requestParam.getGid());
        requestMap.put("orderTag", requestParam.getOrderTag());
        requestMap.put("current", requestParam.getCurrent());
        requestMap.put("size", requestParam.getSize());
        String body = JSON.toJSONString(requestMap);
        String resultPageStr = HttpRequest.post("http://127.0.0.1:8001/api/short-link/v1/page")
                .header("Content-Type", "application/json")
                .body(body)
                .execute()
                .body();
        return JSON.parseObject(resultPageStr, new TypeReference<>() {
        });
    }


    /**
     * Query each group short link count
     *
     * @param requestParam request param group IDs in list
     * @return short link count per group
     */
    default Result<List<ShortLinkGroupCountQueryRespDTO>> listGroupShortLinkCount(List<String> requestParam) {
        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put("requestParam", requestParam);
        String resultBodyStr = HttpUtil.get("http://127.0.0.1:8001/api/short-link/v1/count",
                requestMap);
        return JSON.parseObject(resultBodyStr, new TypeReference<>() {
        });
    }


    /**
     * Update short link
     *
     * @param requestParam short link update request param
     */
    default void updateShortLink(ShortLinkUpdateReqDTO requestParam) {
        HttpUtil.post("http://127.0.0.1:8001/api/short-link/v1/update",
                JSON.toJSONString(requestParam));
    }

    /**
     * Retrieve provided url fetch url website title content in str.
     *
     * @param url provided website url address
     * @return title of the website in str
     */
    default Result<String> getTitleByUrl(String url) {
        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put("url", url);
        String resultStr = HttpUtil.get("http://127.0.0.1:8001/api/short-link/v1/title" +
                "?url=" + url);
        return JSON.parseObject(resultStr, new TypeReference<>() {
        });
    }


    /**
     * Save recycled Short Link Entity
     *
     * @param requestParam short link info to be recycled
     */
    default void saveRecycleBin(RecycleBinSaveReqDTO requestParam) {
        HttpUtil.post("http://127.0.0.1:8001/api/short-link/v1/recycle-bin/save",
                JSON.toJSONString(requestParam));
    }


    /**
     * Page query recycled Short Link
     *
     * @param requestParam paging request param
     * @return short link paging query response body
     */
    default Result<IPage<ShortLinkPageRespDTO>> pageRecycledShortLink(@RequestBody ShortLinkRecycleBinPageReqDTO requestParam) {
        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put("gidList", requestParam.getGidList());
        requestMap.put("current", requestParam.getCurrent());
        requestMap.put("size", requestParam.getSize());
        String body = JSON.toJSONString(requestMap);
        String resultPageStr = HttpRequest.post("http://127.0.0.1:8001/api/short-link/v1" +
                        "/recycle-bin/page")
                .header("Content-Type", "application/json")
                .body(body)
                .execute()
                .body();
        return JSON.parseObject(resultPageStr, new TypeReference<>() {
        });
    }

    /**
     * Short link recover
     *
     * @param requestParam recover short link request param
     */
    default void recoverRecycleBin(RecycleBinRecoverReqDTO requestParam) {
        HttpUtil.post("http://127.0.0.1:8001/api/short-link/v1/recycle-bin/recover",
                JSON.toJSONString(requestParam));
    }

    /**
     * Remove recycled short link item
     *
     * @param requestParam remove short link request param
     */
    default void removeRecycleBin(RecycleBinRemoveReqDTO requestParam) {
        HttpUtil.post("http://127.0.0.1:8001/api/short-link/v1/recycle-bin/remove",
                JSON.toJSONString(requestParam));
    }

    /**
     * Single short link redirection monitor metric records in given start/end date range.
     *
     * @param requestParam short link stats monitor metrics
     * @return monitor statistic records
     */
    default Result<ShortLinkStatsRespDTO> oneShortLinkStats(ShortLinkStatsReqDTO requestParam) {
        String resultBodyStr = HttpUtil.get("http://127.0.0.1:8001/api/short-link/v1/stats", BeanUtil.beanToMap(requestParam));
        return JSON.parseObject(resultBodyStr, new TypeReference<>() {
        });
    }


    /**
     * Fetch specified short link monitor metric record in given date range
     *
     * @param requestParam request parameter
     * @return Short link monitor metric record
     */
    default Result<IPage<ShortLinkStatsAccessRecordRespDTO>> shortLinkStatsAccessRecord(ShortLinkStatsAccessRecordReqDTO requestParam) {
        Map<String, Object> stringObjectMap = BeanUtil.beanToMap(requestParam, false, true);
        stringObjectMap.remove("orders");
        stringObjectMap.remove("records");
        String resultBodyStr = HttpUtil.get("http://127.0.0.1:8001/api/short-link/v1/stats/access-record", stringObjectMap);
        return JSON.parseObject(resultBodyStr, new TypeReference<>() {
        });
    }
}
