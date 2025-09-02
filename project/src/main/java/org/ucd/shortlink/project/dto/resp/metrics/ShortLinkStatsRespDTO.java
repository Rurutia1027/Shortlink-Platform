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

package org.ucd.shortlink.project.dto.resp.metrics;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Short link monitor metrics response body
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShortLinkStatsRespDTO {
    /**
     * Page visitor cnt
     */
    private Integer pv;

    /**
     * Unique visitor cnt
     */
    private Integer uv;

    /**
     * Unique Ip access cnt
     */
    private Integer uip;

    /**
     * Daily basic access statistic metrics
     */
    private List<ShortLinkStatsAccessDailyRespDTO> daily;

    /**
     * Region access statistic metrics
     */
    private List<ShortLinkStatsLocaleRespDTO> localeStats;

    /**
     * Hourly access statistics metrics
     */
    private List<Integer> hourStats;

    /**
     * High frequency access IP info
     */
    private List<ShortLinkStatsTopIpRespDTO> topIpStats;

    /**
     * Weekly access statistics
     */
    private List<Integer> weekdayStats;

    /**
     * Browser access info metrics
     */
    private List<ShortLinkStatsBrowserRespDTO> browserStats;


    /**
     * OS access info metrics
     */
    private List<ShortLinkStatsOsRespDTO> osStats;


    /**
     * Visitor type info metric statistics
     */
    private List<ShortLinkStatsUvRespDTO> uvTypeStats;

    /**
     * Device info metric statistics
     */
    private List<ShortLinkStatsDeviceRespDTO> deviceStats;

    /**
     * Network info metric statistics
     */
    private List<ShortLinkStatsNetworkRespDTO> networkStats;
}
