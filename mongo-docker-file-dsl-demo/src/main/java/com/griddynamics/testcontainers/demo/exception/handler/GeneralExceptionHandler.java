package com.griddynamics.testcontainers.demo.exception.handler;

import com.griddynamics.testcontainers.demo.exception.UserNotFoundException;
import com.griddynamics.testcontainers.demo.exception.UserWithRoleNotExistException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GeneralExceptionHandler {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(UserNotFoundException.class)
    public String handleUserNotFoundException(UserNotFoundException ex) {
        return ex.getMessage();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(UserWithRoleNotExistException.class)
    public String handleRoleNotExistException(UserWithRoleNotExistException ex) {
        return ex.getMessage();
    }
}
