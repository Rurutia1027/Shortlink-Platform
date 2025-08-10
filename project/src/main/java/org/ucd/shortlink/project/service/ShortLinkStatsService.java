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
import org.ucd.shortlink.project.dto.req.ShortLinkGroupStatsReqDTO;
import org.ucd.shortlink.project.dto.req.ShortLinkStatsAccessRecordReqDTO;
import org.ucd.shortlink.project.dto.req.ShortLinkStatsReqDTO;
import org.ucd.shortlink.project.dto.resp.ShortLinkStatsAccessRecordRespDTO;
import org.ucd.shortlink.project.dto.resp.ShortLinkStatsRespDTO;

/**
 * Short link monitor interface layer
 */
public interface ShortLinkStatsService {
    /**
     * Fetch single short link monitor metrics
     *
     * @param requestParam Single Short link monitor metrics query request
     * @return short link monitor metric response b
     */

    ShortLinkStatsRespDTO oneShortLinkStats(ShortLinkStatsReqDTO requestParam);

    /**
     * Fetch short link specified date range monitor metric stats
     *
     * @param requestParam request short link monitor metric request param
     * @return short link monitor metric records
     */
    IPage<ShortLinkStatsAccessRecordRespDTO> shortLinkStatsAccessRecord(ShortLinkStatsAccessRecordReqDTO requestParam);

    /**
     * Fetch short link group monitor metrics
     *
     * @param requestParam short link group metric records request
     * @return grouping short link group metric record response
     */
    ShortLinkStatsRespDTO groupShortLinkStats(ShortLinkGroupStatsReqDTO requestParam);
}
