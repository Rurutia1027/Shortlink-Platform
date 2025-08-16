package org.ucd.shortlink.project.service;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.ucd.shortlink.project.dao.entity.ShortLinkDO;
import org.ucd.shortlink.project.dao.mapper.ShortLinkMapper;
import org.ucd.shortlink.project.dto.req.RecycleBinSaveReqDTO;
import org.ucd.shortlink.project.service.impl.RecycleBinServiceImpl;

import java.lang.reflect.Field;
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



}