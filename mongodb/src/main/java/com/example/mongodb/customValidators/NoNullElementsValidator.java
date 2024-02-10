package com.example.mongodb.customValidators;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Collection;
import java.util.Objects;

public class NoNullElementsValidator implements ConstraintValidator<NoNullElements, Collection<?>> {

    @Override
    public boolean isValid(Collection<?> value, ConstraintValidatorContext context) {
        return Objects.nonNull(value) && value.stream().allMatch(Objects::nonNull);
    }
}
