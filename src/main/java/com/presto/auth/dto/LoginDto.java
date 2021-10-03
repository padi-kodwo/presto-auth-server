package com.presto.auth.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Data
public class LoginDto implements Serializable {
    @Email
    private String email;

    @NotNull
    @Size(min = 8, max = 32)
    private String password;
}
