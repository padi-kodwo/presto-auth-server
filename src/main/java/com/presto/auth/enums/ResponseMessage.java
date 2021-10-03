package com.presto.auth.enums;

import org.springframework.http.HttpStatus;

public enum ResponseMessage {

    SUCCESS(0,"Success"),
    FAILED(-1,"Failed"),
    DATA_NOT_FOUND(HttpStatus.NOT_FOUND.value(),""),
    ACCOUNT_INACTIVE(2101,"Account is not active"),
    INVALID_KYC(513,"Invalid Kyc"),
    SERVER_ERROR(500,""),
    MISSING_PARAMETER(HttpStatus.BAD_REQUEST.value(),"Missing required parameter"),
    PHONE_NUMBER_NOT_MOBILE_NUMBER(2104, "Phone number not a valid mobile number"),
    TOKEN_EXPIRED(1108, "Token expired"),
    INVALID_TOKEN(1109, "Invalid token"),
    INVALID_OTP(1109, "Invalid OTP"),
    ACCOUNT_ALREADY_LINKED(2111, "EDC account already linked");

    private final int code;
    private final String message;

    ResponseMessage(int code, String message){
        this.code = code;
        this.message = message;
    }

    public int getCode(){
        return this.code;
    }

    public String getMessage() {
        return message;
    }

    public static ResponseMessage getByCode(final int code) {
        ResponseMessage result = null;
        for (ResponseMessage roleE : values()) {
            if (roleE.getCode() == code) {
                result = roleE;
                break;
            }
        }
        return result;
    }
}
