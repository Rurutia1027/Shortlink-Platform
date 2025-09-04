package org.ucd.shortlink.project.micrometer.aop;

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
import org.ucd.shortlink.project.service.ShortLinkService;

@Slf4j
@Aspect
@RequiredArgsConstructor
public class ShortLinkUniqueIpAspect {
    private final MeterRegistry registry;
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private ShortLinkService shortLinkService;

    @Around("execution(* org.ucd.shortlink.project.controller..*.restoreUrl(..)) && args" +
            "(shortUri, request, response)")
    public Object trackUniqueIp(ProceedingJoinPoint joinPoint,
                                String shortUri,
                                HttpServletRequest request,
                                HttpServletResponse response) throws Throwable {
        Object result = joinPoint.proceed();
        return result;
    }
}
