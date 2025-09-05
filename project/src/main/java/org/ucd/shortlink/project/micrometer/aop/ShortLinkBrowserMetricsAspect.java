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
import org.ucd.shortlink.project.dto.resp.ShortLinkInfoRespDTO;
import org.ucd.shortlink.project.micrometer.common.constant.MicrometerMetricsConstants;
import org.ucd.shortlink.project.service.ShortLinkService;
import org.ucd.shortlink.project.toolkit.LinkUtil;

@Slf4j
@Aspect
@RequiredArgsConstructor
public class ShortLinkBrowserMetricsAspect {
    private final MeterRegistry registry;

    @Autowired
    private ShortLinkService shortLinkService;

    @Around("execution(* org.ucd.shortlink.project.controller..*.restoreUrl(..)) && args(shortUri, request, response)")
    public Object trackBrowserMetrics(ProceedingJoinPoint joinPoint,
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

        // extract browser from request body
        String browser = LinkUtil.getBrowser(request);
        if (StrUtil.isBlank(browser)) {
            browser = "unknown";
        }

        // Micrometer Counter with tags
        Counter.builder(MicrometerMetricsConstants.METRIC_NAME_SHORTLINK_BROWSER_TYPE_TOTAL)
                .description("Shortlink requests grouped by browser type")
                .tags("job", MicrometerMetricsConstants.JOB_NAME_SHORTLINK_PROJECT,
                        "gid", gid,
                        "fullShortUrl", fullShortUrl,
                        "browser", browser)
                .register(registry)
                .increment();

        return result;
    }
}
