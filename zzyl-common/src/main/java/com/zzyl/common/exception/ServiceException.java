package com.zzyl.common.exception;

import lombok.Getter;

import java.io.Serial;

/**
 * 业务异常
 */
@Getter
public class ServiceException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    private final int code;

    public ServiceException(String message) {
        this(500, message);
    }

    public ServiceException(int code, String message) {
        super(message);
        this.code = code;
    }
}
