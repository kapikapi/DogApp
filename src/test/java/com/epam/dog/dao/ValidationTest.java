package com.epam.dog.dao;

import com.epam.dog.vo.Dog;
import com.epam.dog.vo.DogDto;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.springframework.transaction.annotation.Transactional;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import javax.validation.*;
import java.util.Set;

import static org.testng.Assert.assertEquals;

@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/mvc-dispatcher-servlet.xml"})
public class ValidationTest extends AbstractTransactionalTestNGSpringContextTests{
    
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

    @Transactional
    @Test
    public void checkNameSizeConstraint() {
        Dog dog = createDog(1, "A", 40, 10);
        Set<ConstraintViolation<Dog>> constraintViolations =
                validator.validate(dog);

        assertEquals( 1, constraintViolations.size() );
        assertEquals("size must be between 2 and 200",
                constraintViolations.iterator().next().getMessage()
        );

    }

    @Transactional
    @Test
    public void checkHeightWeightSizeConstraint() {
        Dog dog = createDog(2, "Bbbb", 40, 40);
        Set<ConstraintViolation<Dog>> constraintViolations =
                validator.validate(dog);
        assertEquals(1, constraintViolations.size());
        assertEquals("Height and weight are not balanced!",
                constraintViolations.iterator().next().getMessage()
        );
    }

}