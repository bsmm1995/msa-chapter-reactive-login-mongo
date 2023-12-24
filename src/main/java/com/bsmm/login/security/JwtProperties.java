package com.bsmm.login.security;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {
    private String secretKey;
    private long expirationAt;
    private long expirationRt;
    private String user;
    private String password;
}
