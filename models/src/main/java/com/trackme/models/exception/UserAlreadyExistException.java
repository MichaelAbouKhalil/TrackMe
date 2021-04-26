package com.trackme.models.exception;

import lombok.extern.slf4j.Slf4j;

public class UserAlreadyExistException extends RuntimeException{

    public UserAlreadyExistException(String message) {
        super(message);
    }
}
