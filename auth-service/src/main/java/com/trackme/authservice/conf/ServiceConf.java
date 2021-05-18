package com.trackme.authservice.conf;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Component;

@Component
@Configuration
@Getter
public class ServiceConf {

    private @Value("${jwt.secret}") String jwtSecret;
    private @Value("${verification.token.expiry}") Long verificationTokenExpiry;

}
