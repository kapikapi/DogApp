package com.epam.dog.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;

import static java.time.LocalDate.now;

public class BeforeTodayValidator implements ConstraintValidator<BeforeToday, LocalDate> {

    @Override
    public void initialize(BeforeToday constraintAnnotation) {
        //
    }

    @Override
    public boolean isValid(LocalDate localDate, ConstraintValidatorContext constraintContext) {
        return localDate.isBefore(now());
    }
}
