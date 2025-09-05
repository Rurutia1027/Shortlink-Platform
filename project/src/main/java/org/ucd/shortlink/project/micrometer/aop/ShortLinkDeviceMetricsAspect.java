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
import org.ucd.shortlink.project.micrometer.common.constant.MicrometerMetricsConstatns;
import org.ucd.shortlink.project.service.ShortLinkService;
import org.ucd.shortlink.project.toolkit.LinkUtil;

@Slf4j
@Aspect
@RequiredArgsConstructor
public class ShortLinkDeviceMetricsAspect {
    private final MeterRegistry registry;

    @Autowired
    private ShortLinkService shortLinkService;

    @Around("execution(* org.ucd.shortlink.project.controller..*.restoreUrl(..)) && args" +
            "(shortUri, request, response)")
    public Object trackDeviceMetrics(ProceedingJoinPoint joinPoint,
                                     String shortUri,
                                     HttpServletRequest request,
                                     HttpServletResponse response) throws Throwable {

        if (StrUtil.isNotBlank(shortUri) && shortLinkService.exists(shortUri)) {
            ShortLinkInfoRespDTO resp = shortLinkService.queryShortLinkInfo(shortUri);
            String gid = resp.getGid() != null ? resp.getGid() : "unknown";
            String fullShortUrl = resp.getFullShortUrl() != null ? resp.getFullShortUrl() : "unknown";

            // Parse device type from User-Agent
            String device = LinkUtil.getDevice(request);
            if (StrUtil.isBlank(device)) {
                device = "unknown";
            }

            // Micrometer Counter with tags
            Counter.builder(MicrometerMetricsConstatns.METRIC_NAME_SHORTLINK_DEVICE_TYPE_TOTAL)
                    .description("Shortlink requests grouped by device type (mobile, pc, tablet)")
                    .tags("job", MicrometerMetricsConstatns.JOB_NAME_SHORTLINK_PROJECT,
                            "gid", gid,
                            "fullShortUrl", fullShortUrl,
                            "device", device)
                    .register(registry)
                    .increment();
        }

        return joinPoint.proceed();
    }
}
