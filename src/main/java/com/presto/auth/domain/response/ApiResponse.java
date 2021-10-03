package com.presto.auth.domain.response;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown=true)
public class ApiResponse<T> implements Serializable {

    private int code;
    private String message;
    private String requestId;
    private T data;
    private BaseError error;

}
