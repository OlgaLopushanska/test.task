package org.example.test.task.exception;

public class InvalidRangeException extends RuntimeException {
    private String message;

    public InvalidRangeException(String message) {
        super(message);
    }
}
