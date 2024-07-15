package com.example.time_tracker.util.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

public class UsernameAlreadyExistException extends RuntimeException {
    public UsernameAlreadyExistException() {
        super();
    }
    public UsernameAlreadyExistException(String message, Throwable cause) {
        super(message, cause);
    }
    public UsernameAlreadyExistException(String message) {
        super(message);
    }
    public UsernameAlreadyExistException(Throwable cause) {
        super(cause);
    }
}
