package org.ucd.shortlink.admin.common.constant;

/**
 * short link platform redis cache constants
 */
public class RedisCacheConstant {
    /**
     * User register distributed lock
     */
    public static final String LOCK_USER_REGISTER_KEY = "short-link:lock_user-register:";


    /**
     * User login cache key
     */
    public static final String USER_LOGIN_KEY = "short-link:login:";
}
