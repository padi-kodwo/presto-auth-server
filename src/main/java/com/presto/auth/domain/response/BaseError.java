package com.presto.auth.domain.response;

import lombok.Data;

@Data
public class BaseError {

    private int errorCode;
    private String errorMessage;
    private String url;
}
