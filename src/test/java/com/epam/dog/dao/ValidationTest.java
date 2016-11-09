package com.epam.dog.dao;

import com.epam.dog.DogsHandler;
import com.epam.dog.vo.DogDto;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDate;
import java.util.Set;

import static io.qala.datagen.RandomShortApi.unicode;
import static org.testng.Assert.assertEquals;

public class ValidationTest {

    private static final String NAME_SIZE_VALIDATION_ERROR_MESSAGE = "Size must be between 1 and 100";
    private static final String UNBALANCED_VALIDATION_ERROR_MESSAGE = "Height and weight are not balanced";
    private static final String BIRTH_DATE_VALIDATION_ERROR_MESSAGE = "Date of birth must be earlier than today";

    private static Validator validator;

    @BeforeClass
    public void setValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void failsTooShortNameSizeValidation() {
        DogDto dogDto = DogsHandler.setCorrectDogDto();
        dogDto.setName("");
        checksValidationFail(dogDto, NAME_SIZE_VALIDATION_ERROR_MESSAGE);
    }

    @Test
    public void failsTooLongBorderNameSizeValidation() {
        DogDto dogDto = DogsHandler.setCorrectDogDto();
        dogDto.setName(unicode(101));
        checksValidationFail(dogDto, NAME_SIZE_VALIDATION_ERROR_MESSAGE);
    }

    @Test
    public void failsTooLongNameSizeValidation() {
        checksValidationFail(DogsHandler.setTooLongNameDogDto(), NAME_SIZE_VALIDATION_ERROR_MESSAGE);
    }

    @Test
    public void failsDogBalanceValidation() {
        checksValidationFail(DogsHandler.setUnbalancedDogDto(), UNBALANCED_VALIDATION_ERROR_MESSAGE);
    }

    @Test
    public void failsTodayBirthDateValidation() {
        DogDto dogDto = DogsHandler.setCorrectDogDto();
        dogDto.setDateOfBirth(LocalDate.now());
        checksValidationFail(dogDto, BIRTH_DATE_VALIDATION_ERROR_MESSAGE);
    }

    @Test
    public void failsFutureBirthDateValidation() {
        checksValidationFail(DogsHandler.setFutureBornDogDto(), BIRTH_DATE_VALIDATION_ERROR_MESSAGE);
    }

    @Test
    public void succeedsDogValidation() {
        checksPositiveValidation(DogsHandler.setCorrectDogDto());

        DogDto boundaryDog = DogsHandler.setCorrectDogDto();
        boundaryDog.setName(unicode(1));
        checksPositiveValidation(boundaryDog);

        boundaryDog.setName(unicode(100));
        checksPositiveValidation(boundaryDog);

        boundaryDog.setWeight(boundaryDog.getHeight() - 1);
        checksPositiveValidation(boundaryDog);

        boundaryDog.setWeight(boundaryDog.getHeight() + 1);
        checksPositiveValidation(boundaryDog);

        boundaryDog.setDateOfBirth(LocalDate.now().minusDays(1));
        checksPositiveValidation(boundaryDog);
    }

    private void checksPositiveValidation(DogDto dogDto) {
        Set<ConstraintViolation<DogDto>> constraintViolations =
                validator.validate(dogDto);
        assertEquals(constraintViolations.size(), 0);
    }

    private void checksValidationFail(DogDto dogDto, String message) {
        Set<ConstraintViolation<DogDto>> constraintViolations =
                validator.validate(dogDto);
        assertEquals(1, constraintViolations.size() );
        assertEquals(message, constraintViolations.iterator().next().getMessage());
    }

}