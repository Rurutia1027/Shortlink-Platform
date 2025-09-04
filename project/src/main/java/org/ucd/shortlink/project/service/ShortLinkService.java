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

package org.ucd.shortlink.project.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.RequestParam;
import org.ucd.shortlink.project.dao.entity.ShortLinkDO;
import org.ucd.shortlink.project.dto.req.ShortLinkBatchCreateReqDTO;
import org.ucd.shortlink.project.dto.req.ShortLinkCreateReqDTO;
import org.ucd.shortlink.project.dto.req.ShortLinkPageReqDTO;
import org.ucd.shortlink.project.dto.req.ShortLinkUpdateReqDTO;
import org.ucd.shortlink.project.dto.resp.ShortLinkBatchCreateRespDTO;
import org.ucd.shortlink.project.dto.resp.ShortLinkCreateRespDTO;
import org.ucd.shortlink.project.dto.resp.ShortLinkGroupCountQueryRespDTO;
import org.ucd.shortlink.project.dto.resp.ShortLinkInfoRespDTO;
import org.ucd.shortlink.project.dto.resp.ShortLinkPageRespDTO;

import java.util.List;

/**
 * Short link service layer
 */
public interface ShortLinkService extends IService<ShortLinkDO> {
    /**
     * Create short link
     *
     * @param requestParam create short link object params
     * @return short link creation response info
     */
    ShortLinkCreateRespDTO createShortLink(ShortLinkCreateReqDTO requestParam);

    /**
     * Page query short link items
     *
     * @param requestParam request parameters
     * @return return paging short link response
     */
    IPage<ShortLinkPageRespDTO> pageShortLink(ShortLinkPageReqDTO requestParam);

    /**
     * Query group count each short link group
     *
     * @param requestParam request parameters
     * @return list of group cnt for each query gid
     */
    List<ShortLinkGroupCountQueryRespDTO> listGroupShortLinkCount(@RequestParam List<String> requestParam);

    /**
     * Update Short Link request
     *
     * @param requestParam update short link request
     */
    void updateShortLink(ShortLinkUpdateReqDTO requestParam);


    /**
     * Batch create short link
     *
     * @param requestParam batch create short link request body
     * @return batch create short link response body
     */
    ShortLinkBatchCreateRespDTO batchCreateShortLink(ShortLinkBatchCreateReqDTO requestParam);

    /**
     * Short link redirect
     * @param shortUri short link suffix
     * @param request http request
     * @param response http response
     */
    void restoreUrl(String shortUri, HttpServletRequest request, HttpServletResponse response);

    boolean exists(String shortUri);

    ShortLinkInfoRespDTO queryShortLinkInfo(String shortUri);
}
