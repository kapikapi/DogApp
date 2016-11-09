package com.epam.dog.validation;

import com.epam.dog.vo.DogDto;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class BalanceValidator implements ConstraintValidator<Balance, DogDto> {

    @Override
    public void initialize(Balance constraintAnnotation) {
        //
    }

    @Override
    public boolean isValid(DogDto dog, ConstraintValidatorContext constraintContext) {
        return dog.getWeight() != dog.getHeight();
    }
}