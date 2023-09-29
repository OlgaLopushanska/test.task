package org.example.test.task.exception;

public class UserNotExistsException extends RuntimeException {
    private String message;

    public UserNotExistsException(String message) {
        super(message);
    }
}
