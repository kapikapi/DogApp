package com.epam.dog.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;
import java.lang.annotation.RetentionPolicy;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = BalanceValidator.class)
public @interface Balance {
    String message() default "Height and weight are not balanced";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
}
