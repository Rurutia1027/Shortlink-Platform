package org.ucd.shortlink.project.common.convention.errorcode;

public enum BaseErrorCode implements IErrorCode {
    // ===== First-level macro error code, client side error =====
    CLIENT_ERROR("A000001", "Client error"),


    // ===== Second-level macro error code: user registration error =====
    USER_REGISTER_ERROR("A000100", "User registration error"),
    USER_NAME_VERIFY_ERROR("A000110", "Username verification failed"),
    USER_NAME_EXIST_ERROR("A000111", "Username already exists"),
    USER_NAME_SENSITIVE_ERROR("A000112", "Username contains sensitive words"),
    USER_NAME_SPECIAL_CHARACTER_ERROR("A000113", "Username contains special characters "),
    PASSWORD_VERIFY_ERROR("A000120", "Password verification failed"),
    PASSWORD_SHORT_ERROR("A000121", "Password is too short"),
    PHONE_VERIFY_ERROR("A000151", "Invalid phone number format"),


    // ===== Second-level macro error code: system request missing idempotency token =====
    IDEMPOTENT_TOKEN_NULL_ERROR("A000200", "Idempotent token is missing"),
    IDEMPOTENT_TOKEN_DELETE_ERROR("A000201", "Idempotency token has been used or expired"),

    // ===== Second-level macro error code: The System is currently busy, please try later =====
    FLOW_LIMIT_ERROR("A000300", "The system is currently busy, please try again later"),

    // ===== First-level macro error code: System execution error =====
    SERVICE_ERROR("B000001", "System execution error"),

    // ===== Second-level macro error code: System execution timeout =====
    SERVICE_TIMEOUT_ERROR("B000100", "System execution timeout"),

    // ===== First-level macro error code: Error calling third-party service =====
    REMOTE_ERROR("C000001", "Error occurred while calling third-party service");

    private final String code;
    private final String message;

    BaseErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public String code() {
        return "";
    }

    @Override
    public String message() {
        return "";
    }
}
