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

    private final UserServiceImpl userService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public SeedDB(UserServiceImpl userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(ApplicationArguments args) {
        TenantContext.setCurrentTenant("X-Crossfire");

        if (userService.userCount() > 0)
            return;

        logger.info(" presto_auth ::::::: " + passwordEncoder.encode("secret4auth"));
        logger.info(" presto_app ::::::: " + passwordEncoder.encode("secret4app"));
        logger.info(" presto_web ::::::: " + passwordEncoder.encode("secret4web"));
        logger.info(" presto_orders ::::::: " + passwordEncoder.encode("secret4orders"));
        logger.info(" presto_inventory ::::::: " + passwordEncoder.encode("secret4inventory"));
        logger.info(" presto_deliveries ::::::: " + passwordEncoder.encode("secret4deliveries"));
        logger.info(" presto_payments ::::::: " + passwordEncoder.encode("secret4payments"));
        logger.info(" presto_restaurants ::::::: " + passwordEncoder.encode("secret4restaurants"));
        logger.info(" presto_reviews ::::::: " + passwordEncoder.encode("secret4reviews"));

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
