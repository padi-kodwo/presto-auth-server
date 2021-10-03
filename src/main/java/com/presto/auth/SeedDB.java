package com.presto.auth;

import com.presto.auth.domain.TenantContext;
import com.presto.auth.dto.CreateUserDto;
import com.presto.auth.dto.RoleDto;
import com.presto.auth.service.impl.UserServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
public class SeedDB implements ApplicationRunner {
    private static final Logger logger = LoggerFactory.getLogger(SeedDB.class);


    @Autowired
    private UserServiceImpl userService;


    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(ApplicationArguments args) {
        TenantContext.setCurrentTenant("X-Crossfire");

        if (userService.userCount() > 0)
            return;

        RoleDto roleDto = new RoleDto();
        roleDto.setCode(100);

        CreateUserDto defaultUser = new CreateUserDto();
        defaultUser.setFirstName("Man");
        defaultUser.setOtherNames("User");
        defaultUser.setLastName("Super");
        defaultUser.setPhone("+233235475936");
        defaultUser.setEmail("default@presto.app");
        defaultUser.setPassword(passwordEncoder.encode("123abc123"));
        defaultUser.setRoles(Collections.singletonList(roleDto));

        userService.createUser(defaultUser);
    }
}
