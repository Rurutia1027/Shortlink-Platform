package org.ucd.shortlink.project.common.convention.exception;

import lombok.Getter;
import org.springframework.util.StringUtils;
import org.ucd.shortlink.project.common.convention.errorcode.IErrorCode;

import java.util.Optional;

/**
 * Abstract exception class that encapsulates three main exceptions:
 * - client side exception
 * - server side exception
 * - rpc/remote call exception
 *
 * @see ClientException
 * @see ServiceException
 * @see RemoteException
 */
@Getter
public abstract class AbstractException extends RuntimeException {
    public final String errorCode;
    public final String errorMessage;

    public AbstractException(String message, Throwable throwable, IErrorCode errorCode) {
        super(message, throwable);
        this.errorCode = errorCode.code();
        this.errorMessage = Optional.ofNullable(StringUtils.hasLength(message) ? message :
                null).orElse(errorCode.message());
    }
}
