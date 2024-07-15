package com.example.time_tracker.util.exception;

public class EmailAlreadyExistException extends RuntimeException {
    public EmailAlreadyExistException() {
        super();
    }
    public EmailAlreadyExistException(String message, Throwable cause) {
        super(message, cause);
    }
    public EmailAlreadyExistException(String message) {
        super(message);
    }
    public EmailAlreadyExistException(Throwable cause) {
        super(cause);
    }
}
