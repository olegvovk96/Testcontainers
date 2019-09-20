package com.griddynamics.testcontainers.demo.exception;

public class UserWithRoleNotExistException extends RuntimeException {

    public UserWithRoleNotExistException(String role) {
        super(String.format("Users with role - '%s' doesn't exist", role));
    }
}
