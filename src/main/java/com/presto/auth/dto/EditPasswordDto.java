package com.presto.auth.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class EditPasswordDto {

    @NotNull
    @NotEmpty
    private String id;


    @NotNull
    @Size(min= 8, max = 32)
    private String password;
}
