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

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.ucd.shortlink.project.dto.resp.ShortLinkInfoRespDTO;
import org.ucd.shortlink.project.micrometer.common.constant.MicrometerMetricsConstatns;
import org.ucd.shortlink.project.prometheus.dto.PrometheusQueryReqDTO;
import org.ucd.shortlink.project.prometheus.dto.PrometheusRespDTO;
import org.ucd.shortlink.project.prometheus.service.PrometheusService;
import org.ucd.shortlink.project.service.ShortLinkService;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Aspect
@RequiredArgsConstructor
public class ShortLinkUVMetricsAspect {
    private final MeterRegistry registry;

    @Autowired
    private PrometheusService prometheusService;

    @Autowired
    private ShortLinkService shortLinkService;

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
        String gid = resp.getGid() != null ? resp.getGid() : "unknown";
        String fullShortUrl = resp.getFullShortUrl() != null ? resp.getFullShortUrl() : "unknown";

        // ---- UV Counter ----
        Counter.builder(MicrometerMetricsConstatns.METRIC_NAME_SHORTLINK_UNIQUE_USERS_TOTAL)
                .description("Unique Visitors for short links")
                .tags("job", MicrometerMetricsConstatns.JOB_NAME_SHORTLINK_PROJECT,
                        "gid", gid, "fullShortUrl", fullShortUrl)
                .register(registry)
                .increment();

        // ---- UV Type ----
        boolean isOldUser = checkIfOldUser(gid, fullShortUrl);
        String uvType = isOldUser ? "oldUser" : "newUser";
        Counter.builder("shortlink_uv_type_total")
                .description("Unique visitors by type (new vs old)")
                .tags("gid", gid, "fullShortUrl", fullShortUrl, "uvType", uvType)
                .register(registry)
                .increment();

        return result;
    }

    /**
     * Determine if this gid+fullShortUrl has UV recorded before using PrometheusService
     */
    private boolean checkIfOldUser(String gid, String fullShortUrl) {
        try {
            PrometheusQueryReqDTO queryReq = new PrometheusQueryReqDTO();
            queryReq.setMetricName(MicrometerMetricsConstatns.METRIC_NAME_SHORTLINK_UNIQUE_USERS_TOTAL);
            queryReq.setStartDate("1970-01-01");
            queryReq.setEndDate(DateUtil.now());
            queryReq.setStep("1h");
            queryReq.setJob(MicrometerMetricsConstatns.JOB_NAME_SHORTLINK_PROJECT);

            PrometheusRespDTO resp = prometheusService.queryPrometheusMetric(queryReq);

            if (resp == null || !"success".equals(resp.getStatus()) || resp.getData() == null) {
                return false; // treat as new user if query fails
            }

            // Inspect all results
            for (PrometheusRespDTO.MetricResultDTO metricResult : resp.getData().getResult()) {
                Map<String, String> labels = metricResult.getMetric();
                if (gid.equals(labels.get("gid")) && fullShortUrl.equals(labels.get("fullShortUrl"))) {
                    // If any previous value exists → old user
                    if ((metricResult.getValues() != null && !metricResult.getValues().isEmpty())
                            || (metricResult.getValue() != null && !metricResult.getValue().isEmpty())) {
                        return true;
                    }
                }
            }

            return false; // no previous metric found → new user
        } catch (Exception e) {
            return false; // fail-safe
        }
    }
}
