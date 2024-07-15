package com.example.time_tracker.util.exception;

public class WrongDateOrderException extends RuntimeException {
    public WrongDateOrderException() {
        super();
    }
    public WrongDateOrderException(String message, Throwable cause) {
        super(message, cause);
    }
    public WrongDateOrderException(String message) {
        super(message);
    }
    public WrongDateOrderException(Throwable cause) {
        super(cause);
    }
}
