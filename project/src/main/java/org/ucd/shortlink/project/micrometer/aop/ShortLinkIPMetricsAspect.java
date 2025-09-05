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

package org.ucd.shortlink.project.micrometer.aop;

import cn.hutool.core.util.StrUtil;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.ucd.shortlink.project.common.constant.RedisKeyConstant;
import org.ucd.shortlink.project.dto.resp.ShortLinkInfoRespDTO;
import org.ucd.shortlink.project.micrometer.common.constant.MicrometerMetricsConstants;
import org.ucd.shortlink.project.service.ShortLinkService;

import java.time.LocalDate;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import static org.ucd.shortlink.project.toolkit.LinkUtil.getActualIp;

@Slf4j
@Aspect
@RequiredArgsConstructor
public class ShortLinkIPMetricsAspect {
    private final MeterRegistry registry;
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private ShortLinkService shortLinkService;

    // Cache Gauges to avoid re-registering them every request
    private final ConcurrentHashMap<String, AtomicLong> uipGauges = new ConcurrentHashMap<>();

    @Around("execution(* org.ucd.shortlink.project.controller..*.restoreUrl(..)) && args" +
            "(shortUri, request, response)")
    public Object trackUniqueIp(ProceedingJoinPoint joinPoint,
                                String shortUri,
                                HttpServletRequest request,
                                HttpServletResponse response) throws Throwable {
        Object result = joinPoint.proceed();

        if (StrUtil.isBlank(shortUri) || !shortLinkService.exists(shortUri)) {
            return result;
        }

        // fetch gid + fullShortUrl via given shortUri
        ShortLinkInfoRespDTO respDTO = shortLinkService.queryShortLinkInfo(shortUri);
        String gid = respDTO.getGid() != null ? respDTO.getGid() : "unknown";
        String fullShortUrl = respDTO.getFullShortUrl() != null ? respDTO.getFullShortUrl()
                : "unknown";

        // extract client ip from request
        String clientIp = getActualIp(request);
        if (StrUtil.isBlank(clientIp)) {
            return result;
        }

        // ---- UIP Metric Here ----
        String redisKey = String.format(RedisKeyConstant.REDIS_KEY_STATS_UIP + ":%s:%s:%s",
                fullShortUrl, gid, LocalDate.now());
        Long added = redisTemplate.opsForSet().add(redisKey, clientIp);

        if (added != null && added > 0) {
            Counter.builder(MicrometerMetricsConstants.METRIC_NAME_SHORTLINK_UNIQUE_IP_TOTAL)
                    .description("Unique IP visits for short links")
                    .tags("job", MicrometerMetricsConstants.JOB_NAME_SHORTLINK_PROJECT,
                            "gid", gid, "fullShortUrl", fullShortUrl)
                    .register(registry)
                    .increment();
        }

        // ---- Top IP Counter ----
        Counter.builder(MicrometerMetricsConstants.METRIC_NAME_SHORTLINK_IP_HITS_TOTAL)
                .description("Raw IP hits for short links (used for top IP queries)")
                .tags("job", MicrometerMetricsConstants.JOB_NAME_SHORTLINK_PROJECT,
                        "gid", gid, "fullShortUrl", fullShortUrl, "ip", clientIp)
                .register(registry)
                .increment();

        return result;
    }
}
