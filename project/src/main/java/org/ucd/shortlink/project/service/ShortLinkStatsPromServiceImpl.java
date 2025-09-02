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
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.ucd.shortlink.project.dto.req.metrics.ShortLinkGroupStatsAccessRecordReqDTO;
import org.ucd.shortlink.project.dto.req.metrics.ShortLinkStatsAccessRecordReqDTO;
import org.ucd.shortlink.project.dto.req.metrics.ShortLinkStatsReqDTO;
import org.ucd.shortlink.project.dto.resp.metrics.ShortLinkStatsAccessRecordRespDTO;
import org.ucd.shortlink.project.dto.resp.metrics.ShortLinkStatsRespDTO;
import org.ucd.shortlink.project.prometheus.service.PrometheusService;

/**
 * Short link monitor interface implementor
 */
@Service("PrometheusStatsService")
@RequiredArgsConstructor
public class ShortLinkStatsPromServiceImpl implements ShortLinkStatsService {
    private PrometheusService prometheusService;

    @Override
    public ShortLinkStatsRespDTO oneShortLinkStats(ShortLinkStatsReqDTO requestParam) {
        return null;
    }

    @Override
    public IPage<ShortLinkStatsAccessRecordRespDTO> shortLinkStatsAccessRecord(ShortLinkStatsAccessRecordReqDTO requestParam) {
        return null;
    }

    @Override
    public IPage<ShortLinkStatsAccessRecordRespDTO> groupShortLinkStatsAccessRecord(ShortLinkGroupStatsAccessRecordReqDTO requestParam) {
        return null;
    }
}
