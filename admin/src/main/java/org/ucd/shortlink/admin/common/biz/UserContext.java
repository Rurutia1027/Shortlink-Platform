package org.ucd.shortlink.admin.common.biz;

import com.alibaba.ttl.TransmittableThreadLocal;

import java.util.Optional;

/**
 * User Context
 */
public class UserContext {
    private static final ThreadLocal<UserInfoDTO> USER_THREAD_LOCAL = new TransmittableThreadLocal<>();

    /**
     * Set user info to UserContext
     *
     * @param user context info
     */
    public static void setUser(UserInfoDTO user) {
        USER_THREAD_LOCAL.set(user);
    }

    /**
     * Fetch user ID from UserContext
     *
     * @return User ID
     */
    public static String getUserId() {
        UserInfoDTO userInfoDTO = USER_THREAD_LOCAL.get();
        return Optional.ofNullable(userInfoDTO).map(UserInfoDTO::getUserId).orElse(null);
    }

    /**
     * Fetch username from UserContext
     *
     * @return username
     */
    public static String getUsername() {
        UserInfoDTO userInfoDTO = USER_THREAD_LOCAL.get();
        return Optional.of(userInfoDTO).map(UserInfoDTO::getUsername).orElse(null);
    }

    /**
     * Fetch user real name from UserContext
     *
     * @return user real name
     */
    public static String getRealName() {
        UserInfoDTO userInfoDTO = USER_THREAD_LOCAL.get();
        return Optional.ofNullable(userInfoDTO).map(UserInfoDTO::getRealName).orElse(null);
    }

    /**
     * Clean UserContext
     */
    public static void removeUser() {
        USER_THREAD_LOCAL.remove();
    }
}
