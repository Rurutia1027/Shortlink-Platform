package org.ucd.shortlink.project.service.impl;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.core.StringRedisTemplate;
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
    }

    @Test
    public void testMockInitOk() {
        Assertions.assertNotNull(shortLinkService);
    }

}