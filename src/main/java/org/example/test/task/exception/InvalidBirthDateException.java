package org.example.test.task.exception;


public class InvalidBirthDateException extends RuntimeException {
    private String resourceName;
    private String fieldName;
    private String fieldValue;

    public InvalidBirthDateException(String resourceName, String fieldName, String fieldValue) {
        super(String.format("%s not found with %s : '%s'", resourceName, fieldName, fieldValue));
        this.resourceName = resourceName;
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
    }
}
