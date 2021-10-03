package com.presto.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

@EnableAuthorizationServer
@EnableResourceServer
@SpringBootApplication
@EnableFeignClients
@EnableAsync
@EnableCaching
public class PrestoAuthServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(PrestoAuthServerApplication.class, args);
    }

}
