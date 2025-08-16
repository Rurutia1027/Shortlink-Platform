package org.ucd.shortlink.project.toolkit;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.ucd.shortlink.project.common.constant.ShortLinkConstant.DEFAULT_CACHE_EXPIRE_TIME;

public class LinkUtilTest {
    @Test
    void testGenShortLinkCacheValidDate() {
        Date futureDate = new Date(System.currentTimeMillis() + 1000);
        long diff = LinkUtil.genShortLinkCacheValidDate(futureDate);
        assertTrue(diff > 0);

        long defaultVal = LinkUtil.genShortLinkCacheValidDate(null);
        assertEquals(DEFAULT_CACHE_EXPIRE_TIME, defaultVal);
    }

    @Test
    void testGetActualIp() {
        HttpServletRequest request = mock(HttpServletRequest.class);

        when(request.getHeader("X-Forwarded-For")).thenReturn("1.1.1.1");
        assertEquals("1.1.1.1", LinkUtil.getActualIp(request));

        when(request.getHeader("X-Forwarded-For")).thenReturn(null);
        when(request.getHeader("Proxy-Client-IP")).thenReturn("2.2.2.2");
        assertEquals("2.2.2.2", LinkUtil.getActualIp(request));

        when(request.getHeader(anyString())).thenReturn(null);
        when(request.getRemoteAddr()).thenReturn("3.3.3.3");
        assertEquals("3.3.3.3", LinkUtil.getActualIp(request));
    }

    @Test
    void testGetOs() {
        HttpServletRequest request = mock(HttpServletRequest.class);

        when(request.getHeader("User-Agent")).thenReturn("Mozilla/5.0 (Windows NT 10.0)");
        assertEquals("Windows", LinkUtil.getOs(request));

        when(request.getHeader("User-Agent")).thenReturn("Mozilla/5.0 (iPhone)");
        assertEquals("iOS", LinkUtil.getOs(request));

        when(request.getHeader("User-Agent")).thenReturn("UnknownAgent");
        assertEquals("Unknown", LinkUtil.getOs(request));
    }

    @Test
    void testGetBrowser() {
        HttpServletRequest request = mock(HttpServletRequest.class);

        when(request.getHeader("User-Agent")).thenReturn("Chrome");
        assertEquals("Google Chrome", LinkUtil.getBrowser(request));

        when(request.getHeader("User-Agent")).thenReturn("MSIE");
        assertEquals("Internet Explorer", LinkUtil.getBrowser(request));

        when(request.getHeader("User-Agent")).thenReturn("RandomBrowser");
        assertEquals("Unknown", LinkUtil.getBrowser(request));
    }

    @Test
    void testGetDevice() {
        HttpServletRequest request = mock(HttpServletRequest.class);

        when(request.getHeader("User-Agent")).thenReturn("mobile");
        assertEquals("Mobile", LinkUtil.getDevice(request));

        when(request.getHeader("User-Agent")).thenReturn("Mozilla/5.0");
        assertEquals("PC", LinkUtil.getDevice(request));
    }

    @Test
    void testGetNetwork() {
        HttpServletRequest request = mock(HttpServletRequest.class);

        when(request.getHeader(anyString())).thenReturn(null);
        when(request.getRemoteAddr()).thenReturn("192.168.1.100");
        assertEquals("WIFI", LinkUtil.getNetwork(request));

        when(request.getRemoteAddr()).thenReturn("8.8.8.8");
        assertEquals("Mobile", LinkUtil.getNetwork(request));
    }
}