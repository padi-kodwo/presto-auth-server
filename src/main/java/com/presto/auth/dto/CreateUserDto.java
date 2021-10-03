package com.presto.auth.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class CreateUserDto implements Serializable {

    @NotNull
    @Email
    private String email;

    @NotNull
    @NotEmpty
    private String phone;

    @NotNull
    @NotEmpty
    private String firstName;

    @NotNull
    @Size(min = 8, max = 32)
    private String password;

    @NotNull
    @NotEmpty
    private String lastName;

    private String otherNames;

    @NotNull
    @NotEmpty
    private List<RoleDto> roles = new ArrayList<>();
}
