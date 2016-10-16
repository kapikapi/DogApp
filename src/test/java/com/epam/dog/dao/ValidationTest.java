package com.epam.dog.dao;

import com.epam.dog.vo.Dog;
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

    @Autowired
    private DogDAO dogDAO;

    @Autowired
    private SessionFactory sessionFactory;

    private static Validator validator;

    @BeforeClass
    public void setValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Transactional
    @Test(expectedExceptions = ConstraintViolationException.class)
    public void checkNameConstraintsInSave() {
        Dog savedDog = dogDAO.saveDog("A", 40, 10);
        Set<ConstraintViolation<Dog>> constraintViolations =
                validator.validate(savedDog);

        assertEquals( 1, constraintViolations.size() );
        assertEquals("size must be between 2 and 200",
                constraintViolations.iterator().next().getMessage()
        );

    }

    @Transactional
    @Test(expectedExceptions = ConstraintViolationException.class)
    public void checkNameConstraintsInUpdate() {
        Dog savedDog = dogDAO.saveDog("B", 30, 20);
        Set<ConstraintViolation<Dog>> constraintViolations =
                validator.validate(savedDog);

        assertEquals(1, constraintViolations.size());
        assertEquals("size must be between 2 and 200",
                constraintViolations.iterator().next().getMessage()
        );
    }

}