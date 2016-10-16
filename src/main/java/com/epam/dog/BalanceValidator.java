package com.epam.dog;

import com.epam.dog.vo.Dog;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class BalanceValidator implements ConstraintValidator<Balance, Dog> {

    @Override
    public void initialize(Balance constraintAnnotation) {
        //
    }

    @Override
    public boolean isValid(Dog dog, ConstraintValidatorContext constraintContext) {
        return dog.getWeight() != dog.getHeight();
    }
}