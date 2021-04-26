package com.trackme.authservice.service;

import com.trackme.authservice.repository.UserRepository;
import com.trackme.common.validator.EmailValidator;
import com.trackme.models.security.UserDetail;
import com.trackme.models.security.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserEntity> optionalUserEntity;

        // check if email.
        if(EmailValidator.isEmail(username)){
            optionalUserEntity = userRepository.findByEmail(username);
        }else{
            optionalUserEntity = userRepository.findByUsername(username);
        }

        UserEntity userEntity = optionalUserEntity.orElseThrow(
                () -> new UsernameNotFoundException("Username or Email not found!")
        );

        return new UserDetail(userEntity);
    }
}
