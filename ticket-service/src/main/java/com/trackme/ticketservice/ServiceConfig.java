package com.trackme.ticketservice;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
@Configuration
@Getter
public class ServiceConfig {

    @Value("${jwt.secret}")
    private String jwtSecretKey;
}
