package com.trackme.authservice.service;

import com.trackme.authservice.conf.ServiceConf;
import com.trackme.authservice.repository.VerificationTokenRepository;
import com.trackme.models.exception.NotFoundException;
import com.trackme.models.security.UserEntity;
import com.trackme.models.security.VerificationToken;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class VerificationTokenService {

    private final VerificationTokenRepository verificationTokenRepository;
    private final ServiceConf serviceConf;

    public void createVerificationToken(UserEntity user, String token){
        log.info("saving registration token for user [{}]", user.getEmail());

        VerificationToken verificationToken = VerificationToken.builder()
                .token(token).user(user)
                .expiryDate(LocalDateTime.now()
                        .atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
                + serviceConf.getVerificationTokenExpiry())
                .build();

        verificationTokenRepository.save(verificationToken);
    }

    public VerificationToken getVerificationToken(String token) {
        log.info("retrieving verification token");

        VerificationToken verificationToken = verificationTokenRepository.findByTokenAndUsedFalse(token)
                .orElseThrow(() -> new NotFoundException("token already used or does not exist"));

        return verificationToken;
    }

    @Transactional
    public void deleteToken(String token){
        log.info("removing token [{}]", token);

        verificationTokenRepository.deleteByToken(token);
    }
}
