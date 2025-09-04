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
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.ucd.shortlink.project.common.constant.RedisKeyConstant;
import org.ucd.shortlink.project.dto.resp.ShortLinkInfoRespDTO;
import org.ucd.shortlink.project.service.ShortLinkService;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import static org.ucd.shortlink.project.toolkit.LinkUtil.getActualIp;

@Slf4j
@Aspect
@RequiredArgsConstructor
public class ShortLinkUniqueIpAspect {
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

        // Store into Redis Set TODO: refine this with PFADD for hyperloglog
        String redisKey =
                RedisKeyConstant.REDIS_KEY_STATS_UIP + ":" + gid + ":" + fullShortUrl;
        redisTemplate.opsForSet().add(redisKey, clientIp);
        Long uipSize = redisTemplate.opsForSet().size(redisKey);
        long uipCount = (uipSize != null) ? uipSize : 0L;

        // register / update Gauge
        String gaugeKey = gid + ":" + fullShortUrl;
        AtomicLong gaugeValue = uipGauges.computeIfAbsent(gaugeKey, key -> {
            AtomicLong holder = new AtomicLong(0);
            Gauge.builder("shortlink_unique_ip_total", holder, AtomicLong::get)
                    .description("Unique IPs accessing the short link")
                    .tags("gid", gid, "fullShortUrl", fullShortUrl)
                    .register(registry);
            return holder;
        });

        gaugeValue.set(uipCount);

        return result;
    }
}
