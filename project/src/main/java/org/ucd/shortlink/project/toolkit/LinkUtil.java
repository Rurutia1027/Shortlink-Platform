package org.ucd.shortlink.project.toolkit;

import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;

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
}
