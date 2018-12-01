package com.mdd.admin.config.mybatisplus.mybatisplus;

/**
 * @Desc:
 * @Author Maduo
 * @Create 2018/12/1 16:07
 */
public class MpException extends RuntimeException {

    public MpException() {
    }

    public MpException(String message) {
        super(message);
    }

    public MpException(String message, Throwable cause) {
        super(message, cause);
    }

    public MpException(Throwable cause) {
        super(cause);
    }

    public MpException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
