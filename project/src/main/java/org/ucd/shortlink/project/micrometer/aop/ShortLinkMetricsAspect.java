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
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.ucd.shortlink.project.dto.resp.ShortLinkInfoRespDTO;
import org.ucd.shortlink.project.micrometer.common.constant.MicrometerMetricsConstatns;
import org.ucd.shortlink.project.service.ShortLinkService;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import static org.ucd.shortlink.project.common.constant.RedisKeyConstant.REDIS_KEY_SHORT_LINK_RESTORE_UV_KEY;

@Aspect
@RequiredArgsConstructor
public class ShortLinkMetricsAspect {
    private final MeterRegistry registry;

    @Autowired
    private ShortLinkService shortLinkService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    // Cache Gauges to avoid re-registering them every request
    private final ConcurrentHashMap<String, AtomicLong> uvGauges = new ConcurrentHashMap<>();

    @Around("execution(* org.ucd.shortlink.project.controller..*.restoreUrl(..)) && args(shortUri, request, response)")
    public Object trackShortLinkTotalPv(ProceedingJoinPoint joinPoint,
                                        String shortUri,
                                        HttpServletRequest request,
                                        HttpServletResponse response) throws Throwable {
        Object result = joinPoint.proceed();
        if (StrUtil.isBlank(shortUri) || !shortLinkService.exists(shortUri)) {
            return result;
        }

        ShortLinkInfoRespDTO resp = shortLinkService.queryShortLinkInfo(shortUri);
        String gid = resp.getGid();
        String fullShortUrl = resp.getFullShortUrl();

        // UV Gauge (snapshot value from Redis set)
        String redisKey = String.format(REDIS_KEY_SHORT_LINK_RESTORE_UV_KEY + ":%s:%s",
                gid, fullShortUrl);
        Long uvSize = stringRedisTemplate.opsForSet().size(redisKey);
        long uvCount = (uvSize != null) ? uvSize : 0;
        // Use cached AtomicLong for Prometheus gauge
        String gaugeKey = gid + ":" + fullShortUrl;
        AtomicLong gaugeValue = uvGauges.computeIfAbsent(gaugeKey, key -> {
            AtomicLong holder = new AtomicLong(0);
            Gauge.builder(MicrometerMetricsConstatns.METRIC_NAME_SHORTLINK_SHORT_URL_UNIQUE_USERS_TOTAL_METRIC_NAME,
                            holder, AtomicLong::get)
                    .description("Unique visitors for short links")
                    .tags("gid", gid, "fullShortUrl", fullShortUrl)
                    .register(registry);
            return holder;
        });
        gaugeValue.set(uvCount);
        return result;
    }
}
