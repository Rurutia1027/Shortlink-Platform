package org.ucd.shortlink.project.service;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.ucd.shortlink.project.dao.mapper.ShortLinkMapper;
import org.ucd.shortlink.project.service.impl.RecycleBinServiceImpl;

import static org.junit.jupiter.api.Assertions.*;

class RecycleBinServiceTest {
    @Mock
    private ShortLinkMapper shortLinkMapper;

    @Mock
    private StringRedisTemplate stringRedisTemplate;

    @InjectMocks
    private RecycleBinServiceImpl recycleBinService; 


}