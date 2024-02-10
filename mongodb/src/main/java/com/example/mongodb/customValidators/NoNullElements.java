package com.example.mongodb.customValidators;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Constraint(validatedBy = NoNullElementsValidator.class)
@Retention(RetentionPolicy.RUNTIME)
public @interface NoNullElements {

    String message() default "The input list cannot be null nor contain null elements.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
