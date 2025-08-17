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

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.ucd.shortlink.project.dao.entity.LinkAccessLogsDO;
import org.ucd.shortlink.project.dao.entity.LinkAccessStatsDO;
import org.ucd.shortlink.project.dao.entity.LinkDeviceStatsDO;
import org.ucd.shortlink.project.dao.entity.LinkLocaleStatsDO;
import org.ucd.shortlink.project.dao.entity.LinkNetworkStatsDO;
import org.ucd.shortlink.project.dao.mapper.LinkAccessLogsMapper;
import org.ucd.shortlink.project.dao.mapper.LinkAccessStatsMapper;
import org.ucd.shortlink.project.dao.mapper.LinkBrowserStatsMapper;
import org.ucd.shortlink.project.dao.mapper.LinkDeviceStatsMapper;
import org.ucd.shortlink.project.dao.mapper.LinkLocaleStatsMapper;
import org.ucd.shortlink.project.dao.mapper.LinkNetworkStatsMapper;
import org.ucd.shortlink.project.dao.mapper.LinkOsStatsMapper;
import org.ucd.shortlink.project.dto.req.ShortLinkGroupStatsAccessRecordReqDTO;
import org.ucd.shortlink.project.dto.req.ShortLinkStatsAccessRecordReqDTO;
import org.ucd.shortlink.project.dto.req.ShortLinkStatsReqDTO;
import org.ucd.shortlink.project.dto.resp.ShortLinkStatsAccessRecordRespDTO;
import org.ucd.shortlink.project.dto.resp.ShortLinkStatsRespDTO;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

public class ShortLinkStatsServiceImplTest {
    @Mock
    private LinkAccessStatsMapper linkAccessStatsMapper;
    @Mock
    private LinkLocaleStatsMapper linkLocaleStatsMapper;
    @Mock
    private LinkAccessLogsMapper linkAccessLogsMapper;
    @Mock
    private LinkBrowserStatsMapper linkBrowserStatsMapper;
    @Mock
    private LinkOsStatsMapper linkOsStatsMapper;
    @Mock
    private LinkDeviceStatsMapper linkDeviceStatsMapper;
    @Mock
    private LinkNetworkStatsMapper linkNetworkStatsMapper;


    private ShortLinkStatsService shortLinkStatsService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        shortLinkStatsService = new ShortLinkStatsServiceImpl(
                linkAccessStatsMapper,
                linkLocaleStatsMapper,
                linkAccessLogsMapper,
                linkBrowserStatsMapper,
                linkOsStatsMapper,
                linkDeviceStatsMapper,
                linkNetworkStatsMapper);
    }

    @Test
    public void testMockInitOk() {
        Assertions.assertNotNull(shortLinkStatsService);
    }

    @Test
    public void testOneShortLinkStats() {
        ShortLinkStatsReqDTO requestParam = ShortLinkStatsReqDTO
                .builder()
                .startDate(DateUtil.formatDate(new Date()))
                .endDate(DateUtil.formatDate(new Date()))
                .gid(UUID.randomUUID().toString())
                .fullShortLink(UUID.randomUUID().toString())
                .build();

        when(linkAccessStatsMapper.listStatsByShortLink(any(ShortLinkStatsReqDTO.class)))
                .thenReturn(List.of(LinkAccessStatsDO.builder()
                        .date(new Date())
                        .fullShortUrl(UUID.randomUUID().toString())
                        .gid(UUID.randomUUID().toString())
                        .hour(8)
                        .id(1L)
                        .pv(1)
                        .uv(1)
                        .uip(1)
                        .weekday(2)
                        .build()));

        when(linkAccessLogsMapper.findPvUvUidStatsByShortLink(any(ShortLinkStatsReqDTO.class)))
                .thenReturn(LinkAccessStatsDO.builder()
                        .uip(1)
                        .weekday(1)
                        .uv(1)
                        .pv(1)
                        .id(1L)
                        .gid(UUID.randomUUID().toString())
                        .fullShortUrl(UUID.randomUUID().toString())
                        .date(new Date())
                        .build());

        when(linkLocaleStatsMapper.listLocaleByShortLink(any(ShortLinkStatsReqDTO.class)))
                .thenReturn(List.of(LinkLocaleStatsDO.builder()
                        .city(UUID.randomUUID().toString())
                        .adcode(UUID.randomUUID().toString())
                        .cnt(1)
                        .date(new Date())
                        .gid(UUID.randomUUID().toString())
                        .province(UUID.randomUUID().toString())
                        .build()));

        when(linkAccessStatsMapper.listHourStatsByShortLink(any(ShortLinkStatsReqDTO.class)))
                .thenReturn(List.of(LinkAccessStatsDO.builder()
                        .date(new Date())
                        .fullShortUrl(UUID.randomUUID().toString())
                        .gid(UUID.randomUUID().toString())
                        .pv(1)
                        .uv(1)
                        .weekday(3)
                        .hour(3)
                        .id(1L)
                        .build()));
        HashMap<String, Object> topIpByShortLinkMap = new HashMap<>();
        topIpByShortLinkMap.put("ip", "127.0.0.1");
        topIpByShortLinkMap.put("count", "2");
        when(linkAccessLogsMapper.listTopIpByShortLink(any(ShortLinkStatsReqDTO.class)))
                .thenReturn(List.of(topIpByShortLinkMap));

        when(linkAccessStatsMapper.listWeekdayStatsByShortLink(any(ShortLinkStatsReqDTO.class)))
                .thenReturn(List.of(LinkAccessStatsDO.builder()
                        .id(1L)
                        .hour(1)
                        .weekday(3)
                        .uv(1)
                        .pv(1)
                        .gid(UUID.randomUUID().toString())
                        .fullShortUrl(UUID.randomUUID().toString())
                        .date(new Date())
                        .uip(1)
                        .build()));

        HashMap<String, Object> browserMap = new HashMap<>();
        browserMap.put("count", 4);
        browserMap.put("browser", "Chrome");
        when(linkBrowserStatsMapper.listBrowserStatsByShortLink(any(ShortLinkStatsReqDTO.class)))
                .thenReturn(List.of(browserMap));

        HashMap<String, Object> osMap = new HashMap<>();
        osMap.put("count", "9");
        osMap.put("os", "mac");
        when(linkOsStatsMapper.listOsStatsByShortLink(any(ShortLinkStatsReqDTO.class)))
                .thenReturn(List.of(osMap));

        HashMap<String, Object> uvTypeMap = new HashMap<>();
        uvTypeMap.put("oldUserCnt", "8");
        uvTypeMap.put("newUserCnt", "19");
        when(linkAccessLogsMapper.findUvTypeCntByShortLink(any(ShortLinkStatsReqDTO.class)))
                .thenReturn(new HashMap<>());

        when(linkDeviceStatsMapper.listDeviceStatsByShortLink(any(ShortLinkStatsReqDTO.class)))
                .thenReturn(List.of(LinkDeviceStatsDO
                        .builder()
                        .cnt(1)
                        .date(new Date())
                        .device(UUID.randomUUID().toString())
                        .fullShortUrl(UUID.randomUUID().toString())
                        .gid(UUID.randomUUID().toString())
                        .id(1L)
                        .build()));

        when(linkNetworkStatsMapper.listNetworkStatsByShortLink(any(ShortLinkStatsReqDTO.class)))
                .thenReturn(List.of(LinkNetworkStatsDO.builder()
                        .cnt(1)
                        .date(new Date())
                        .fullShortUrl(UUID.randomUUID().toString())
                        .gid(UUID.randomUUID().toString())
                        .id(1L)
                        .network(UUID.randomUUID().toString())
                        .build()));

        ShortLinkStatsRespDTO respDTO = shortLinkStatsService.oneShortLinkStats(requestParam);
        Assertions.assertNotNull(respDTO);
        Assertions.assertNotNull(respDTO.getBrowserStats());
        Assertions.assertTrue(respDTO.getBrowserStats().size() > 0);
        Assertions.assertNotNull(respDTO.getDaily());
        Assertions.assertTrue(respDTO.getDaily().size() > 0);
        Assertions.assertTrue(respDTO.getUv() > 0);
        Assertions.assertTrue(respDTO.getUip() > 0);
        Assertions.assertTrue(respDTO.getPv() > 0);
    }

    @Test
    public void testShortLinkStatsAccessRecord() {
        ShortLinkStatsAccessRecordReqDTO requestParam = ShortLinkStatsAccessRecordReqDTO
                .builder()
                .startDate(DateUtil.formatDate(new Date()))
                .endDate(DateUtil.formatDate(new Date()))
                .gid(UUID.randomUUID().toString())
                .fullShortUrl(UUID.randomUUID().toString())
                .build();
        IPage<LinkAccessLogsDO> queryPageResp = new Page<LinkAccessLogsDO>()
                .setCurrent(1L)
                .setSize(1L)
                .setTotal(10L)
                .setRecords(List.of(LinkAccessLogsDO
                        .builder()
                        .browser(UUID.randomUUID().toString())
                        .device(UUID.randomUUID().toString())
                        .gid(UUID.randomUUID().toString())
                        .id(1L)
                        .fullShortUrl(UUID.randomUUID().toString())
                        .os(UUID.randomUUID().toString())
                        .user(UUID.randomUUID().toString())
                        .locale(UUID.randomUUID().toString())
                        .ip(UUID.randomUUID().toString())
                        .network(UUID.randomUUID().toString())
                        .build()));
        when(linkAccessLogsMapper.selectPage(any(), any())).thenReturn(queryPageResp);
        IPage<ShortLinkStatsAccessRecordRespDTO> response =
                shortLinkStatsService.shortLinkStatsAccessRecord(requestParam);

        HashMap<String, Object> uvTypeMap = new HashMap<>();
        uvTypeMap.put("user", UUID.randomUUID().toString());
        uvTypeMap.put("uvType", UUID.randomUUID().toString());

        when(linkAccessLogsMapper.selectUvTypeByUsers(
                anyString(),
                anyString(),
                anyString(),
                anyString(),
                anyList()
        )).thenReturn(List.of(uvTypeMap));
        Assertions.assertNotNull(response);
        Assertions.assertTrue(response.getRecords().size() > 0);
        Assertions.assertTrue(response.getCurrent() > 0L);
        Assertions.assertTrue(response.getTotal() > 0L);
        Assertions.assertTrue(response.getPages() > 0L);
        Assertions.assertTrue(response.getSize() > 0L);
    }

    @Test
    public void testGroupShortLinkStatsAccessRecord() {
        // prepare for mock test data
        IPage<LinkAccessLogsDO> queryPageResp = new Page<LinkAccessLogsDO>()
                .setCurrent(2L)
                .setTotal(100L)
                .setSize(10L)
                .setRecords(List.of(LinkAccessLogsDO.builder().build()));
        when(linkAccessLogsMapper.selectPage(any(), any())).thenReturn(queryPageResp);
        Map<String, Object> uvTypeMap = new HashMap<>();
        uvTypeMap.put("user", UUID.randomUUID().toString());
        when(linkAccessLogsMapper.selectGroupUvTypeByUsers(anyString(), anyString(),
                anyString(), anyList())).thenReturn(List.of(uvTypeMap));

        ShortLinkGroupStatsAccessRecordReqDTO requestParam =
                ShortLinkGroupStatsAccessRecordReqDTO
                        .builder()
                        .startDate(DateUtil.formatDate(new Date()))
                        .endDate(DateUtil.formatDate(new Date()))
                        .gid(UUID.randomUUID().toString())
                        .build();
        IPage<ShortLinkStatsAccessRecordRespDTO> pageQueryResp =
                shortLinkStatsService.groupShortLinkStatsAccessRecord(requestParam);
        Assertions.assertNotNull(pageQueryResp);
    }
}