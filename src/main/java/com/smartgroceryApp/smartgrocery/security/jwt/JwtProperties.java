package com.smartgroceryApp.smartgrocery.security.jwt;

import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Getter @Setter
@Validated
@ConfigurationProperties(prefix = "app.jwt")
public class JwtProperties {
    private String secret;

    @Positive
    private long accessTokenExpiration;

    @Positive
    private long refreshTokenExpiration;
}
