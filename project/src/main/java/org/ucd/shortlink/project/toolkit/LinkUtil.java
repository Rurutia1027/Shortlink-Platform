package org.ucd.shortlink.project.toolkit;

import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Date;
import java.util.Optional;

import static org.ucd.shortlink.project.common.constant.ShortLinkConstant.DEFAULT_CACHE_EXPIRE_TIME;

/**
 * Short Link Util class
 */
public class LinkUtil {
    /**
     * Fetch short link item valid date value in MS
     *
     * @param validDate Short link item's validDate field, custom: not-null, permanent: null
     *
     * @return validate date value in million-seconds
     */
    public static long genShortLinkCacheValidDate(Date validDate) {
        return Optional.ofNullable(validDate).map(item -> DateUtil.between(new Date(), item,
                DateUnit.MS)).orElse(DEFAULT_CACHE_EXPIRE_TIME);
    }

    /**
     * Fetch User Real IP Address
     *
     * @param request request
     * @return User Real IP Address
     */
    public static String getActualIp(HttpServletRequest request) {
        String ipAddress = request.getHeader("X-Forwarded-For");
        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getRemoteAddr();
        }
        return ipAddress;
    }
}
