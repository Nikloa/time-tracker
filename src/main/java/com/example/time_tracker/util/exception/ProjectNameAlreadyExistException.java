package com.example.time_tracker.util.exception;

import jakarta.validation.constraints.NotBlank;

public class ProjectNameAlreadyExistException extends RuntimeException {
    public ProjectNameAlreadyExistException() {
        super();
    }
    public ProjectNameAlreadyExistException(String message, Throwable cause) {
        super(message, cause);
    }
    public ProjectNameAlreadyExistException(String message) {
        super(message);
    }
    public ProjectNameAlreadyExistException(Throwable cause) {
        super(cause);
    }

}
