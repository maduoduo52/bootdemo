package com.mdd.admin.config.exception;

/**
 * @Desc:
 * @Author Maduo
 * @Create 2018/12/1 16:58
 */
public class NeedLoginException extends RuntimeException {

    public NeedLoginException() {
        super();
    }

    public NeedLoginException(String message) {
        super(message);
    }

    public NeedLoginException(String message, Throwable cause) {
        super(message, cause);
    }

    public NeedLoginException(Throwable cause) {
        super(cause);
    }

    protected NeedLoginException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
