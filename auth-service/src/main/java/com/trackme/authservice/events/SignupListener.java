package com.trackme.authservice.events;

import com.trackme.authservice.service.AuthUserService;
import com.trackme.authservice.service.VerificationTokenService;
import com.trackme.models.security.UserEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class SignupListener implements ApplicationListener<OnSignUpCompleteEvent> {

    private final VerificationTokenService tokenService;

    @Override
    public void onApplicationEvent(OnSignUpCompleteEvent event) {
        this.confirmSignup(event);
    }

    private void confirmSignup(OnSignUpCompleteEvent event) {
        UserEntity user = event.getUser();
        String token = UUID.randomUUID().toString();
        tokenService.createVerificationToken(user, token);

        // TODO add sending email via service
    }
}
