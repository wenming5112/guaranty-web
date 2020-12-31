package com.example.guaranty.common.exception;

import lombok.extern.slf4j.Slf4j;

/**
 * Business Exception
 *
 * @author ming
 * @version 1.0.0
 * @date 2020/04/15
 **/
@Slf4j
public class BusinessException extends Exception {
    private static final long serialVersionUID = 1782066458001959902L;

    public BusinessException() {
        super();
    }

    public BusinessException(String message) {
        super(message);
    }

    public BusinessException(Throwable cause) {
        super(cause);
    }

    public BusinessException(String message, Throwable cause) {
        super(message, cause);
    }
}
