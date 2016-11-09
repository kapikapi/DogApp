package com.epam.dog.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = BeforeTodayValidator.class)
public @interface BeforeToday {
    String message() default "Date of birth must be earlier than today";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
}