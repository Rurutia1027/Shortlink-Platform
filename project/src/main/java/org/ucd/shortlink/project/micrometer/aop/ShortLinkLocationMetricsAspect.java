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
import org.ucd.shortlink.project.dao.entity.GeoIpInfoDO;
import org.ucd.shortlink.project.dto.resp.ShortLinkInfoRespDTO;
import org.ucd.shortlink.project.micrometer.common.constant.MicrometerMetricsConstants;
import org.ucd.shortlink.project.service.GeoIpService;
import org.ucd.shortlink.project.service.ShortLinkService;
import org.ucd.shortlink.project.toolkit.LinkUtil;

@Slf4j
@Aspect
@RequiredArgsConstructor
public class ShortLinkLocationMetricsAspect {
    private final MeterRegistry registry;

    @Autowired
    private GeoIpService geoIpService;

    @Autowired
    private ShortLinkService shortLinkService;

    @Around("execution(* org.ucd.shortlink.project.controller..*.restoreUrl(..)) && args" +
            "(shortUri, request, response)")
    public Object trackLocale(ProceedingJoinPoint joinPoint,
                              String shortUri,
                              HttpServletRequest request,
                              HttpServletResponse response) throws Throwable {
        Object result = joinPoint.proceed();
        if (StrUtil.isBlank(shortUri) || !shortLinkService.exists(shortUri)) {
            return result;
        }

        // Query short link info
        ShortLinkInfoRespDTO respDTO = shortLinkService.queryShortLinkInfo(shortUri);
        String gid = respDTO.getGid() != null ? respDTO.getGid() : "unknown";
        String fullShortUrl = respDTO.getFullShortUrl() != null ? respDTO.getFullShortUrl() : "unknown";

        // Extract client IP
        String clientIp = LinkUtil.getActualIp(request);
        if (StrUtil.isBlank(clientIp)) {
            return result;
        }

        // Resolve locale via GeoIpService
        GeoIpInfoDO geoIpInfoDO = geoIpService.resolveFull(clientIp);
        if (geoIpInfoDO == null) {
            log.warn("No specific locale info fetched via ip {} build blank one", clientIp);
            geoIpInfoDO = GeoIpInfoDO.builder()
                    .country("UNKNOWN")
                    .countryCode("UNKNOWN")
                    .province("UNKNOWN")
                    .city("UNKNOWN")
                    .adcode("UNKNOWN")
                    .build();
        }

        // ---- Locale Metric ----
        Counter.builder(MicrometerMetricsConstants.METRIC_NAME_SHORTLINK_LOCALE_TYPE_TOTAL)
                .description("Requests by geo location for short links")
                .tags("job", MicrometerMetricsConstants.JOB_NAME_SHORTLINK_PROJECT,
                        "gid", gid,
                        "fullShortUrl", fullShortUrl,
                        "country", geoIpInfoDO.getCountryCode(),
                        "province", geoIpInfoDO.getProvince(),
                        "city", geoIpInfoDO.getCity())
                .register(registry)
                .increment();

        log.debug("Recorded geo metric gid={} url={} ip={} -> {}",
                gid, fullShortUrl, clientIp, geoIpInfoDO);

        return result;
    }
}
