package org.ucd.shortlink.admin.common.convention.result;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;

@Data
@Accessors(chain = true)
public class Result<T> implements Serializable {
    @Serial
    private static final long serialVersionUID = 9374349023727L;

    /**
     * success code
     */
    public static final String SUCCESS_CODE = "0";

    /**
     * response code
     */
    private String code;

    /**
     * response message
     */
    private String message;

    /**
     * respond data
     */
    private T data;

    /**
     * request id
     * todo:
     * - In complex distributed call systems, this is commonly used as a anchor and is
     * - bound to the full trace ID for correlation purposes.
     */
    private String requestId;

    public boolean isSuccess() {
        return SUCCESS_CODE.equals(code);
    }
}
