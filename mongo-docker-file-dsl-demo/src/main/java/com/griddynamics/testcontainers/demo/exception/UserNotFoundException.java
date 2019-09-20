package com.griddynamics.testcontainers.demo.exception;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(String userId) {
        super(String.format("User with id - '%s' doesn't exist", userId));
    }
}
