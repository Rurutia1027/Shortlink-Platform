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

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RedissonClient;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.ucd.shortlink.project.common.convention.exception.ClientException;
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
import org.ucd.shortlink.project.dto.req.ShortLinkBatchCreateReqDTO;
import org.ucd.shortlink.project.dto.req.ShortLinkCreateReqDTO;
import org.ucd.shortlink.project.dto.req.ShortLinkPageReqDTO;
import org.ucd.shortlink.project.dto.req.ShortLinkUpdateReqDTO;
import org.ucd.shortlink.project.dto.resp.ShortLinkBatchCreateRespDTO;
import org.ucd.shortlink.project.dto.resp.ShortLinkCreateRespDTO;
import org.ucd.shortlink.project.dto.resp.ShortLinkGroupCountQueryRespDTO;
import org.ucd.shortlink.project.dto.resp.ShortLinkPageRespDTO;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.ucd.shortlink.project.common.constant.RedisKeyConstant.REDIRECT_IS_BLANK_SHORT_LINK_KEY;

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

    @Test
    public void testRestoreUrl_with_OriginalUrl_Cached() {
        String shortUri = UUID.randomUUID().toString();
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        when(request.getServletPath()).thenReturn("localhost");
        when(request.getServerPort()).thenReturn(8005);
        when(request.getHeader("User-Agent")).thenReturn("mac");
        when(request.getHeader("X-Forwarded-For")).thenReturn("127.0.0.1");


        ValueOperations<String, String> valueOperations = mock(ValueOperations.class);
        SetOperations<String, String> setOperations = mock(SetOperations.class);
        when(stringRedisTemplate.opsForValue()).thenReturn(valueOperations);
        when(stringRedisTemplate.opsForSet()).thenReturn(setOperations);
        when(setOperations.add(anyString())).thenReturn(1L);
        when(valueOperations.get(anyString())).thenReturn("https://baidu.com");
        when(shortLinkRouteMapper.selectOne(any())).thenReturn(ShortLinkRouteDO.builder()
                .gid(UUID.randomUUID().toString())
                .build());
        doNothing().when(linkAccessStatsMapper).shortLinkStats(any());
        doNothing().when(linkLocaleStatsMapper).shortLinkLocaleState(any());
        doNothing().when(linkOsStatsMapper).shortLinkOsState(any());
        doNothing().when(linkBrowserStatsMapper).shortLinkBrowserState(any());
        doNothing().when(linkNetworkStatsMapper).shortLinkNetworkState(any());

        when(linkAccessLogsMapper.insert(any())).thenReturn(1);
        when(linkDeviceStatsMapper.insert(any())).thenReturn(1);
        doNothing().when(shortLinkMapper).incrementStats(anyString(), anyString(), anyInt(),
                any(), any());
        doNothing().when(linkStatsTodayMapper).shortLinkTodayState(any());
        shortLinkService.restoreUrl(shortUri, request, response);
    }

    @Test
    public void testRestoreUrl_with_Page_Not_Found() {
        String shortUri = UUID.randomUUID().toString();
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        when(request.getServletPath()).thenReturn("localhost");
        when(request.getServerPort()).thenReturn(8005);
        when(request.getHeader("User-Agent")).thenReturn("mac");
        when(request.getHeader("X-Forwarded-For")).thenReturn("127.0.0.1");


        ValueOperations<String, String> valueOperations = mock(ValueOperations.class);
        SetOperations<String, String> setOperations = mock(SetOperations.class);
        when(stringRedisTemplate.opsForValue()).thenReturn(valueOperations);
        when(stringRedisTemplate.opsForSet()).thenReturn(setOperations);
        when(setOperations.add(anyString())).thenReturn(1L);
        when(valueOperations.get(anyString())).thenReturn("");
        when(shortLinkRouteMapper.selectOne(any())).thenReturn(ShortLinkRouteDO.builder()
                .gid(UUID.randomUUID().toString())
                .build());
        doNothing().when(linkAccessStatsMapper).shortLinkStats(any());
        doNothing().when(linkLocaleStatsMapper).shortLinkLocaleState(any());
        doNothing().when(linkOsStatsMapper).shortLinkOsState(any());
        doNothing().when(linkBrowserStatsMapper).shortLinkBrowserState(any());
        doNothing().when(linkNetworkStatsMapper).shortLinkNetworkState(any());

        when(linkAccessLogsMapper.insert(any())).thenReturn(1);
        when(linkDeviceStatsMapper.insert(any())).thenReturn(1);
        doNothing().when(shortLinkMapper).incrementStats(anyString(), anyString(), anyInt(),
                any(), any());
        doNothing().when(linkStatsTodayMapper).shortLinkTodayState(any());

        when(shortUriCreationCachePenetrationBloomFilter.contains(anyString())).thenReturn(false);
        shortLinkService.restoreUrl(shortUri, request, response);
    }

    @Test
    public void testRestoreUrl_with_Redirect_Url_Blank() {
        String shortUri = UUID.randomUUID().toString();
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        when(request.getServletPath()).thenReturn("localhost");
        when(request.getServerPort()).thenReturn(8005);
        when(request.getHeader("User-Agent")).thenReturn("mac");
        when(request.getHeader("X-Forwarded-For")).thenReturn("127.0.0.1");


        ValueOperations<String, String> valueOperations = mock(ValueOperations.class);
        SetOperations<String, String> setOperations = mock(SetOperations.class);
        when(stringRedisTemplate.opsForValue()).thenReturn(valueOperations);
        when(stringRedisTemplate.opsForSet()).thenReturn(setOperations);
        when(setOperations.add(anyString())).thenReturn(1L);
        when(valueOperations.get(REDIRECT_IS_BLANK_SHORT_LINK_KEY)).thenReturn("");
        when(shortLinkRouteMapper.selectOne(any())).thenReturn(ShortLinkRouteDO.builder()
                .gid(UUID.randomUUID().toString())
                .build());
        doNothing().when(linkAccessStatsMapper).shortLinkStats(any());
        doNothing().when(linkLocaleStatsMapper).shortLinkLocaleState(any());
        doNothing().when(linkOsStatsMapper).shortLinkOsState(any());
        doNothing().when(linkBrowserStatsMapper).shortLinkBrowserState(any());
        doNothing().when(linkNetworkStatsMapper).shortLinkNetworkState(any());

        when(linkAccessLogsMapper.insert(any())).thenReturn(1);
        when(linkDeviceStatsMapper.insert(any())).thenReturn(1);
        doNothing().when(shortLinkMapper).incrementStats(anyString(), anyString(), anyInt(),
                any(), any());
        doNothing().when(linkStatsTodayMapper).shortLinkTodayState(any());
        shortLinkService.restoreUrl(shortUri, request, response);
    }

    @Test
    public void testPageShortLink() {
        ShortLinkPageReqDTO requestParam = ShortLinkPageReqDTO.builder()
                .build();
        IPage<ShortLinkDO> page = new Page<>();
        ShortLinkDO item1 = ShortLinkDO.builder().domain("item-1-domain").build();
        ShortLinkDO item2 = ShortLinkDO.builder().domain("item-2-domain").build();
        page.setCurrent(1L);
        page.setTotal(2L);
        page.setSize(2L);
        page.setRecords(List.of(item1, item2));
        when(shortLinkMapper.pageLink(any())).thenReturn(page);

        IPage<ShortLinkPageRespDTO> response = shortLinkService.pageShortLink(requestParam);
        Assertions.assertNotNull(response);
        Assertions.assertTrue(response.getCurrent() > 0L);
        Assertions.assertTrue(response.getPages() > 0L);
        Assertions.assertTrue(response.getSize() > 0L);
        Assertions.assertTrue(response.getTotal() > 0L);
        Assertions.assertTrue(response.getRecords().size() > 0);
    }

    @Test
    public void testListGroupShortLinkCount() {
        List<String> requestParams = List.of(UUID.randomUUID().toString(),
                UUID.randomUUID().toString(), UUID.randomUUID().toString());

        Map<String, Object> map1 = new HashMap<>();
        map1.put("gid", UUID.randomUUID().toString());
        map1.put("shortLinkCount", 4);
        Map<String, Object> map2 = new HashMap<>();
        map2.put("gid", UUID.randomUUID().toString());
        map2.put("shortLinkCount", 4);

        when(shortLinkMapper.selectMaps(any())).thenReturn(List.of(map1, map2));
        List<ShortLinkGroupCountQueryRespDTO> response =
                shortLinkService.listGroupShortLinkCount(requestParams);
        Assertions.assertNotNull(response);
        Assertions.assertTrue(StrUtil.isNotBlank(response.get(1).getGid()));
        Assertions.assertNotNull(response.get(1).getShortLinkCount() > 0);
    }

    @Test
    public void testUpdateShortLink_NO_ShortLink_Found() {
        ShortLinkUpdateReqDTO requestParam = ShortLinkUpdateReqDTO.builder().build();
        when(shortLinkMapper.selectOne(any())).thenReturn(null);
        ClientException exception = assertThrows(ClientException.class, () -> {
            shortLinkService.updateShortLink(requestParam);
        });
    }

    @Test
    public void testUpdateShortLink_HAS_ShortLink_Found_Gid_NOT_Match() {
        Date validateDate = new Date();
        ShortLinkUpdateReqDTO requestParam = ShortLinkUpdateReqDTO
                .builder()
                .validDateType(ValiDateTypeEnum.PERMANENT.getType())
                .validDate(validateDate)
                .gid(UUID.randomUUID().toString())
                .build();

        ShortLinkDO existing = ShortLinkDO.builder()
                .gid(UUID.randomUUID().toString())
                .fullShortUrl(UUID.randomUUID().toString())
                .validDateType(ValiDateTypeEnum.CUSTOM.getType())
                .validDate(validateDate)
                .build();

        when(shortLinkMapper.selectOne(any(LambdaQueryWrapper.class))).thenReturn(existing);
        when(shortLinkMapper.update(any(ShortLinkDO.class), any(LambdaUpdateWrapper.class))).thenReturn(1);
        when(stringRedisTemplate.delete(anyString())).thenReturn(true);
        shortLinkService.updateShortLink(requestParam);
    }

    @Test
    public void testBatchCreateShortLink() {
        ShortLinkBatchCreateReqDTO requestParam = ShortLinkBatchCreateReqDTO
                        .builder()
                        .createdType(1)
                        .describes(List.of(UUID.randomUUID().toString(), UUID.randomUUID().toString()))
                        .gid(UUID.randomUUID().toString())
                        .validDate(new Date())
                        .validDateType(ValiDateTypeEnum.PERMANENT.getType())
                        .originUrls(List.of("https://baidu.com", "https://baidu.com"))
                        .build();
        when(shortLinkMapper.insert(any())).thenReturn(1);
        when(shortLinkRouteMapper.insert(any())).thenReturn(1);

        ValueOperations valueOperations = mock(ValueOperations.class);
        doNothing().when(valueOperations).set(anyString(), anyString());
        when(stringRedisTemplate.opsForValue()).thenReturn(valueOperations);
        when(shortUriCreationCachePenetrationBloomFilter.add(anyString())).thenReturn(true);
        ShortLinkBatchCreateRespDTO response =
                shortLinkService.batchCreateShortLink(requestParam);

        Assertions.assertNotNull(response);
        Assertions.assertTrue(response.getBaseLinkInfos().size() > 0);
        Assertions.assertTrue(response.getTotal() > 0);
        Assertions.assertTrue(StrUtil.isNotBlank(response.getBaseLinkInfos().get(0).getOriginUrl()));
    }
}