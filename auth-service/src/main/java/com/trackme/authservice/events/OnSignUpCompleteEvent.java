package com.trackme.authservice.events;

import com.trackme.models.security.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.context.ApplicationEvent;

public class OnSignUpCompleteEvent extends ApplicationEvent {
    private UserEntity user;

    public OnSignUpCompleteEvent(UserEntity user) {
        super(user);
        this.user = user;
    }

    public UserEntity getUser() {
        return user;
    }
}
