package com.example.guaranty.common.exception;

/**
 * Redis 缓存异常
 *
 * @author ming
 * @version 1.0.0
 * @date 2020/4/16 11:52
 **/
public class RedisCacheException extends Exception {
    private static final long serialVersionUID = 3911801907781791924L;

    public RedisCacheException(String message) {
        super(message);
    }
}
