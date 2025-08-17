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
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.ucd.shortlink.project.dao.entity.ShortLinkDO;
import org.ucd.shortlink.project.dao.mapper.ShortLinkMapper;
import org.ucd.shortlink.project.dto.req.RecycleBinRecoverReqDTO;
import org.ucd.shortlink.project.dto.req.RecycleBinRemoveReqDTO;
import org.ucd.shortlink.project.dto.req.RecycleBinSaveReqDTO;
import org.ucd.shortlink.project.dto.req.ShortLinkRecycleBinPageReqDTO;
import org.ucd.shortlink.project.dto.resp.ShortLinkPageRespDTO;

import java.lang.reflect.Field;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class RecycleBinServiceImplTest {
    @Mock
    private ShortLinkMapper shortLinkMapper;

    @Mock
    private StringRedisTemplate stringRedisTemplate;

    private RecycleBinServiceImpl recycleBinService;

    @SneakyThrows
    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        recycleBinService = new RecycleBinServiceImpl(stringRedisTemplate);

        // inject baseMapper using reflection
        Field baseMapperField =
                recycleBinService.getClass().getSuperclass().getDeclaredField("baseMapper");
        baseMapperField.setAccessible(true);
        baseMapperField.set(recycleBinService, shortLinkMapper);
    }

    @Test
    public void testMockInitOk() {
        Assertions.assertNotNull(shortLinkMapper);
        Assertions.assertNotNull(stringRedisTemplate);
        Assertions.assertNotNull(recycleBinService);
    }

    @Test
    public void testSaveRecycleBin() {
        RecycleBinSaveReqDTO requestParam = RecycleBinSaveReqDTO.builder()
                .fullShortUrl("https://" + UUID.randomUUID().toString() + ".com")
                .gid(UUID.randomUUID().toString())
                .build();
        when(shortLinkMapper.update(any(ShortLinkDO.class), any())).thenReturn(1);
        when(stringRedisTemplate.delete(anyString())).thenReturn(true);
        recycleBinService.saveRecycleBin(requestParam);

        verify(shortLinkMapper).update(any(), any());
        verify(stringRedisTemplate).delete(anyString());
    }

    @Test
    public void testPageShortLInk() {
        ShortLinkRecycleBinPageReqDTO requestParam = ShortLinkRecycleBinPageReqDTO.builder()
                .gidList(List.of(UUID.randomUUID().toString(), UUID.randomUUID().toString(),
                        UUID.randomUUID().toString()))
                .build();
        ShortLinkDO shortLinkDO = ShortLinkDO.builder()
                .totalUip(UUID.randomUUID().variant())
                .totalPv(UUID.randomUUID().variant())
                .domain(UUID.randomUUID().toString())
                .domain(UUID.randomUUID().toString())
                .gid(UUID.randomUUID().toString())
                .build();
        IPage<ShortLinkDO> shortLinkDOPage =
                new Page<ShortLinkDO>().setCurrent(1L).setSize(1L).setTotal(1L).setRecords(List.of(shortLinkDO));
        when(shortLinkMapper.selectPage(any(), any())).thenReturn(shortLinkDOPage);

        IPage<ShortLinkPageRespDTO> responseDTO =
                recycleBinService.pageShortLink(requestParam);
        Assertions.assertNotNull(responseDTO);
        Assertions.assertEquals(responseDTO.getCurrent(), 1L);
        Assertions.assertNotNull(responseDTO.getRecords());
        Assertions.assertEquals(responseDTO.getRecords().size(), 1);
    }

    @Test
    public void testRecoverRecycleBin() {
        RecycleBinRecoverReqDTO requestParam = RecycleBinRecoverReqDTO
                .builder()
                .fullShortUrl(UUID.randomUUID().toString())
                .gid(UUID.randomUUID().toString())
                .build();

        recycleBinService.recoverRecycleBin(requestParam);
        when(shortLinkMapper.update(any(ShortLinkDO.class), any())).thenReturn(1);
        when(stringRedisTemplate.delete(anyString())).thenReturn(true);
    }

    @Test
    public void testRemoveRecycleBin() {
        RecycleBinRemoveReqDTO requestParam = RecycleBinRemoveReqDTO
                .builder()
                .gid(UUID.randomUUID().toString())
                .fullShortUrl(UUID.randomUUID().toString())
                .build();
        recycleBinService.removeRecycleBin(requestParam);
        when(shortLinkMapper.delete(any())).thenReturn(1);
    }
}