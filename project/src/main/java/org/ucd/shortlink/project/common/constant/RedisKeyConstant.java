package org.ucd.shortlink.project.common.constant;

/**
 * Redis key constant class
 */
public class RedisKeyConstant {
    /**
     * Short link redirection cache key
     */
    public static final String REDIRECT_SHORT_LINK_KEY = "short-link_redirect_%s";

    /**
     * Short link blank redirection address cache key
     */
    public static final String REDIRECT_IS_BLANK_SHORT_LINK_KEY = "short" +
            "-link_is_blank_redirect_%s";

    /**
     * Short link redirection distributed lock (via Redis)
     */
    public static final String LOCK_REDIRECT_SHORT_LINK_KEY = "short-link_lock_redirect_%s";
}
