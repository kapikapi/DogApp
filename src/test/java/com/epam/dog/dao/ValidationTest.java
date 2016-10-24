package com.epam.dog.dao;

import com.epam.dog.vo.Dog;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import javax.validation.*;
import java.util.Set;

import static io.qala.datagen.RandomShortApi.positiveInteger;
import static io.qala.datagen.RandomShortApi.unicode;
import static org.testng.Assert.assertEquals;

public class ValidationTest {

    private static Validator validator;

    @BeforeClass
    public void setValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    private Dog createDog(int id, String name, int height, int weight) {
        Dog dog = new Dog();
        dog.setId(id);
        dog.setName(name);
        dog.setHeight(height);
        dog.setWeight(weight);
        return dog;
    }

    @Test
    public void failsNameSizeValidation() {
        Dog dog = createDog(positiveInteger(), unicode(0, 1), positiveInteger(), positiveInteger());
        Set<ConstraintViolation<Dog>> constraintViolations =
                validator.validate(dog);

        assertEquals(1, constraintViolations.size() );
        assertEquals("size must be between 2 and 200",
                constraintViolations.iterator().next().getMessage()
        );

    }

    @Test
    public void failsDogBalanceValidation() {
        int height = positiveInteger();
        Dog dog = createDog(positiveInteger(), unicode(2, 200), height, height);
        Set<ConstraintViolation<Dog>> constraintViolations =
                validator.validate(dog);
        assertEquals(1, constraintViolations.size());
        assertEquals("Height and weight are not balanced!",
                constraintViolations.iterator().next().getMessage()
        );
    }

    @Test
    public void succeedsDogValidation() {
        int height = positiveInteger();
        int weight = positiveInteger();
        if (height == weight) {
            weight = height + 1;
        }
        Dog dog = createDog(positiveInteger(), unicode(2, 200), height, weight);
        Set<ConstraintViolation<Dog>> constraintViolations =
                validator.validate(dog);
        assertEquals(0, constraintViolations.size() );
    }

}