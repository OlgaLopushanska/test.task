package org.example.test.task.utils;


import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;


public class CustomDateValidator implements
        ConstraintValidator<CustomDateConstraint, String> {

    private static final String DATE_PATTERN = "yyyy-MM-dd";
    @Value("${user.birthdate.constraint}")
    private int constraint;

    @Override
    public void initialize(CustomDateConstraint customDate) {
    }

    @Override
    public boolean isValid(String customDateField,
                           ConstraintValidatorContext cxt) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_PATTERN);
        try {
            LocalDate localDate = LocalDate.parse(customDateField, formatter);
            return LocalDate.now().getYear() - localDate.getYear() - constraint > 0;
        } catch (DateTimeParseException e) {
            return false;
        }
    }
}
