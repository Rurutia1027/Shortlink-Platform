package org.ucd.shortlink.project.common.convention.exception;


import org.ucd.shortlink.project.common.convention.errorcode.BaseErrorCode;
import org.ucd.shortlink.project.common.convention.errorcode.IErrorCode;

/**
 * Encapsulates client side exception
 */
public class ClientException extends AbstractException {
    public ClientException(IErrorCode errorCode) {
        this(null, null, errorCode);
    }

    public ClientException(String message) {
        this(message, null, BaseErrorCode.CLIENT_ERROR);
    }

    public ClientException(String message, Throwable throwable, IErrorCode errorCode) {
        super(message, throwable, errorCode);
    }

    @Override
    public String toString() {
        return "ClientException{" +
                "code='" + errorCode + "'," +
                "message='" + errorMessage + "'" +
                '}';
    }
}
