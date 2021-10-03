package com.presto.auth.dto;

import com.presto.auth.enums.SocialAuthProvider;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
public class SocialSignUpUserDto implements Serializable {

    @Email
    private String email;

    @NotNull
    private SocialAuthProvider socialAuthProvider;
}
