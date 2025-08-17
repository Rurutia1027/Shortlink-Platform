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

import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RedissonClient;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.ucd.shortlink.project.common.convention.exception.ServiceException;
import org.ucd.shortlink.project.common.enums.ValiDateTypeEnum;
import org.ucd.shortlink.project.dao.entity.ShortLinkDO;
import org.ucd.shortlink.project.dao.entity.ShortLinkRouteDO;
import org.ucd.shortlink.project.dao.mapper.LinkAccessLogsMapper;
import org.ucd.shortlink.project.dao.mapper.LinkAccessStatsMapper;
import org.ucd.shortlink.project.dao.mapper.LinkBrowserStatsMapper;
import org.ucd.shortlink.project.dao.mapper.LinkDeviceStatsMapper;
import org.ucd.shortlink.project.dao.mapper.LinkLocaleStatsMapper;
import org.ucd.shortlink.project.dao.mapper.LinkNetworkStatsMapper;
import org.ucd.shortlink.project.dao.mapper.LinkOsStatsMapper;
import org.ucd.shortlink.project.dao.mapper.LinkStatsTodayMapper;
import org.ucd.shortlink.project.dao.mapper.ShortLinkMapper;
import org.ucd.shortlink.project.dao.mapper.ShortLinkRouteMapper;
import org.ucd.shortlink.project.dto.req.ShortLinkCreateReqDTO;
import org.ucd.shortlink.project.dto.resp.ShortLinkCreateRespDTO;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ShortLinkServiceImplTest {
    @Mock
    private RBloomFilter<String> shortUriCreationCachePenetrationBloomFilter;

    @Mock
    private ShortLinkMapper shortLinkMapper;

    @Mock
    private ShortLinkRouteMapper shortLinkRouteMapper;

    @Mock
    private StringRedisTemplate stringRedisTemplate;

    @Mock
    private RedissonClient redissonClient;

    @Mock
    private LinkAccessStatsMapper linkAccessStatsMapper;

    @Mock
    private LinkLocaleStatsMapper linkLocaleStatsMapper;

    @Mock
    private LinkOsStatsMapper linkOsStatsMapper;

    @Mock
    private LinkBrowserStatsMapper linkBrowserStatsMapper;

    @Mock
    private LinkAccessLogsMapper linkAccessLogsMapper;

    @Mock
    private LinkDeviceStatsMapper linkDeviceStatsMapper;

    @Mock
    private LinkNetworkStatsMapper linkNetworkStatsMapper;

    @Mock
    private LinkStatsTodayMapper linkStatsTodayMapper;

    private ShortLinkServiceImpl shortLinkService;

    @SneakyThrows
    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        shortLinkService = new ShortLinkServiceImpl(
                shortUriCreationCachePenetrationBloomFilter,
                shortLinkRouteMapper,
                stringRedisTemplate,
                redissonClient,
                linkAccessStatsMapper,
                linkLocaleStatsMapper,
                linkOsStatsMapper,
                linkBrowserStatsMapper,
                linkAccessLogsMapper,
                linkDeviceStatsMapper,
                linkNetworkStatsMapper,
                linkStatsTodayMapper
        );
        Field baseMapperField =
                shortLinkService.getClass().getSuperclass().getDeclaredField("baseMapper");
        baseMapperField.setAccessible(true);
        baseMapperField.set(shortLinkService, shortLinkMapper);
    }

    @Test
    public void testMockInitOk() {
        Assertions.assertNotNull(shortLinkService);
    }

    @Test
    public void testCreateShortLink_with_ShortUriDB_IS_Available() {
        ShortLinkCreateReqDTO requestParam = ShortLinkCreateReqDTO.builder()
                .createdType(1)
                .domain(UUID.randomUUID().toString())
                .describe(UUID.randomUUID().toString())
                .originUrl("https://baidu.com")
                .gid(UUID.randomUUID().toString())
                .validDate(new Date())
                .validDateType(ValiDateTypeEnum.PERMANENT.getType())
                .build();
        when(shortUriCreationCachePenetrationBloomFilter.contains(anyString())).thenReturn(false);
        when(shortLinkRouteMapper.insert(any(ShortLinkRouteDO.class))).thenReturn(1);
        ValueOperations<String, String> valueOperations = mock(ValueOperations.class);
        when(stringRedisTemplate.opsForValue()).thenReturn(valueOperations);
        doNothing().when(valueOperations).set(
                anyString(),
                anyString(),
                anyLong(),
                any(TimeUnit.class)
        );
        when(shortUriCreationCachePenetrationBloomFilter.add(anyString())).thenReturn(true);
        ShortLinkCreateRespDTO shortLinkCreateRespDTO = shortLinkService.createShortLink(requestParam);
        Assertions.assertNotNull(shortLinkCreateRespDTO);
        Assertions.assertNotNull(shortLinkCreateRespDTO.getFullShortUrl());
        Assertions.assertNotNull(shortLinkCreateRespDTO.getGid());
        Assertions.assertNotNull(shortLinkCreateRespDTO.getOriginalUrl());
    }

    @Test
    public void testCreateShortLink_with_ShortUriDB_NOT_Available() {
        ShortLinkCreateReqDTO requestParam = ShortLinkCreateReqDTO.builder()
                .createdType(1)
                .domain(UUID.randomUUID().toString())
                .describe(UUID.randomUUID().toString())
                .originUrl("https://baidu.com")
                .gid(UUID.randomUUID().toString())
                .validDate(new Date())
                .validDateType(ValiDateTypeEnum.PERMANENT.getType())
                .build();
        when(shortLinkMapper.insert(any())).thenThrow(new DuplicateKeyException("duplicate " +
                "key"));
        when(shortLinkMapper.selectOne(any())).thenReturn(ShortLinkDO.builder().build());

        ServiceException exception = assertThrows(ServiceException.class, () -> {
            shortLinkService.createShortLink(requestParam);
        });
    }
}