package com.trackme.models.exception;

public class InvalidRoleException extends RuntimeException{

    public InvalidRoleException(String message) {
        super(message);
    }
}
