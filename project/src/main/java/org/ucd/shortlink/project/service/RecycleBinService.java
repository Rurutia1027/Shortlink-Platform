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
import org.ucd.shortlink.project.dao.entity.ShortLinkDO;
import org.ucd.shortlink.project.dto.req.RecycleBinRecoverReqDTO;
import org.ucd.shortlink.project.dto.req.RecycleBinRemoveReqDTO;
import org.ucd.shortlink.project.dto.req.RecycleBinSaveReqDTO;
import org.ucd.shortlink.project.dto.req.ShortLinkRecycleBinPageReqDTO;
import org.ucd.shortlink.project.dto.resp.ShortLinkPageRespDTO;

/**
 * Short Link Recycle Bin Service interface layer
 */
public interface RecycleBinService extends IService<ShortLinkDO> {
    /**
     * Save recycled Short Link Entity
     *
     * @param requestParam short link info to be recycled
     */
    void saveRecycleBin(RecycleBinSaveReqDTO requestParam);


    /**
     * Page query recycled short link items
     *
     * @param requestParam request parameters
     * @return return paging short link response
     */
    IPage<ShortLinkPageRespDTO> pageShortLink(ShortLinkRecycleBinPageReqDTO requestParam);

    /**
     * Recover recycled short link item
     *
     * @param requestParam recover request parameters
     */
    void recoverRecycleBin(RecycleBinRecoverReqDTO requestParam);

    /**
     * Delete recycled short link item
     *
     * @param requestParam remove request parameters
     */
    void removeRecycleBin(RecycleBinRemoveReqDTO requestParam);
}
