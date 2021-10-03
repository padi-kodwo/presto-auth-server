package com.presto.auth.dto;

import com.presto.auth.enums.UserStatus;
import lombok.Data;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Data
public class UserDto implements Serializable {
    private String id;
    private String email;
    private String phone;
    private String firstName;
    private String lastName;
    private String otherNames;
    private String onboardStatus;
    private String lastLogin;
    private String lastSeen;
    private UserStatus userStatus;
    private int successiveFailedAttempts;
    private boolean loggedIn;
    private String created;
    private String updated;
    private Set<RoleDto> roles = new HashSet<>();
}
