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
public class ShortLinkOsMetricsAspect {
    private final MeterRegistry registry;

    @Autowired
    private ShortLinkService shortLinkService;

    @Around("execution(* org.ucd.shortlink.project.controller..*.restoreUrl(..)) && args(shortUri, request, response)")
    public Object trackOsMetrics(ProceedingJoinPoint joinPoint,
                                 String shortUri,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Throwable {
        if (StrUtil.isNotBlank(shortUri) && shortLinkService.exists(shortUri)) {
            ShortLinkInfoRespDTO resp = shortLinkService.queryShortLinkInfo(shortUri);
            String gid = resp.getGid() != null ? resp.getGid() : "unknown";
            String fullShortUrl = resp.getFullShortUrl() != null ? resp.getFullShortUrl() : "unknown";

            // Parse OS from User-Agent
            String os = LinkUtil.getOs(request);
            if (StrUtil.isBlank(os)) {
                os = "unknown";
            }

            // Micrometer Counter with tags
            Counter.builder(MicrometerMetricsConstatns.METRIC_NAME_SHORTLINK_OS_TOTAL)
                    .description("Shortlink requests grouped by operating system")
                    .tags("job", MicrometerMetricsConstatns.JOB_NAME_SHORTLINK_PROJECT,
                            "gid", gid,
                            "fullShortUrl", fullShortUrl,
                            "os", os)
                    .register(registry)
                    .increment();
        }

        return joinPoint.proceed();
    }
}
