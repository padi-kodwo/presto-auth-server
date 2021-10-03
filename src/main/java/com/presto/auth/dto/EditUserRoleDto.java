package com.presto.auth.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.List;

@Data
public class EditUserRoleDto implements Serializable {

    @NotEmpty
    private List<RoleDto> roles;
    @NotEmpty
    private String userPhone;
}
