package com.presto.auth.exception;

public class MissingParameterException extends RuntimeException {


    public MissingParameterException() {
    }

    public MissingParameterException(String message) {
        super(message);
    }

    public MissingParameterException(String message, Throwable cause) {
        super(message, cause);
    }

    public MissingParameterException(Throwable cause) {
        super(cause);
    }

    public MissingParameterException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
