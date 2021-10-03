package com.presto.auth.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Map;

@Data
@ConfigurationProperties(prefix = "auth.system.config")
@Component
public class AuthProperties implements Serializable {
    private Map<String, Map<String, String>> messages;
}

