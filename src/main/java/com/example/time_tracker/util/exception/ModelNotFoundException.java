package com.example.time_tracker.util.exception;

public class ModelNotFoundException extends RuntimeException {
    public ModelNotFoundException() {
        super();
    }
    public ModelNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
    public ModelNotFoundException(String message) {
        super(message);
    }
    public ModelNotFoundException(Throwable cause) {
        super(cause);
    }
}
