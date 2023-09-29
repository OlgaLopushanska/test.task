package org.example.test.task.utils;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;


import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = CustomDateValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface CustomDateConstraint {

    String message() default "Invalid date format, it should be yyyy-MM-dd and age older than 18";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
