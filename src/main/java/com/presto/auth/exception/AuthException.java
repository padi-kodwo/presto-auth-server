package com.presto.auth.exception;


public class AuthException extends RuntimeException {

    private int code;
    private String message;

    public AuthException(int code, String message){
        this.code = code;
        this.message = message;
    }


    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
