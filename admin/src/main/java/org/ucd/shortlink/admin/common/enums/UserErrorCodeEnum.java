package org.ucd.shortlink.admin.common.enums;

import org.ucd.shortlink.admin.common.convention.errorcode.IErrorCode;

/**
 * User error code enumeration.
 */
public enum UserErrorCodeEnum implements IErrorCode {
    USER_NULL("B000200", "User record does not exist"),
    USER_NAME_EXIST("B000201", "User name already exist"),
    USER_EXIST("B000202", "User record already exists"),
    USER_SAVE_ERROR("B000203", "User save failure");

    private final String code;

    private final String message;

    UserErrorCodeEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public String code() {
        return code;
    }

    @Override
    public String message() {
        return message;
    }
}
