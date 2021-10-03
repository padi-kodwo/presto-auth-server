package com.presto.auth.exception;

public class UnauthorizedException extends RuntimeException {

    private String apiKey;

    public UnauthorizedException(String apiKey){
        this.apiKey = apiKey;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    @Override
    public String getMessage() {
        return "Client Unauthorized";
    }

}
