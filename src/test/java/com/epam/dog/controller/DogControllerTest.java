package com.epam.dog.controller;

import com.epam.dog.DogsHandler;
import com.epam.dog.dao.DogDAO;
import com.epam.dog.vo.Dog;
import com.epam.dog.vo.DogDto;
import io.restassured.http.ContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static io.qala.datagen.RandomShortApi.positiveInteger;
import static io.qala.datagen.RandomShortApi.unicode;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.core.IsEqual.equalTo;

@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/mvc-dispatcher-servlet.xml"})
public class DogControllerTest extends AbstractTransactionalTestNGSpringContextTests {

    @Autowired
    private DogDAO dogDAO;

    @DataProvider(name = "firstId")
    public static Object[][] shouldSetFirstDogsId() {
        return new Object[][]{
                {"id", 1}
        };
    }

    @DataProvider(name = "secondId")
    public static Object[][] shouldSetSecondDogsId() {
        return new Object[][]{
                {"id", 2}
        };
    }

    @DataProvider(name = "thirdId")
    public static Object[][] shouldSetThirdDogsId() {
        return new Object[][]{
                {"id", 3}
        };
    }

    @Test(dataProvider = "thirdId")
    public void shouldGetDogById(String paramName, int id) {
        DogDto newDog = saveDog();
        given().
                pathParam(paramName, id).
                when().
                get("/dog/{id}").
                then().
                assertThat()
                .statusCode(200)
                .body("name", equalTo(newDog.getName()))
                .body("height", equalTo(newDog.getHeight()))
                .body("weight", equalTo(newDog.getWeight()));
    }

    @Test()
    public void shouldGetAllDogs() {
        for (Dog dog : dogDAO.getAllDogs()) {
            given().
                    when().
                    get("/dog").
                    then().
                    assertThat()
                    .contentType(ContentType.JSON)
                    .statusCode(200)
                    .body("name", hasItem(dog.getName()))
                    .body("height", hasItem(dog.getHeight()))
                    .body("weight", hasItem(dog.getWeight()));
        }
    }

    @DataProvider(name = "newDog")
    public Object[][] shouldSetNewDogData() {
        DogDto hamilton = DogsHandler.setRandomDogDto();
        return new Object[][]{
                {"dog", hamilton}
        };
    }

    @Test(dataProvider = "newDog")
    public void shouldSaveSpecifiedDog(String paramName, DogDto newDog) {
        given()
                .contentType(ContentType.JSON)
                .body(newDog)
                .when()
                .post("/dog")
                .then()
                .statusCode(201);
    }

    @Test(dataProvider = "secondId")
    public void shouldUpdateDogById(String paramName, int id) {
        DogDto luhu = DogsHandler.setRandomDogDto();
        given()
                .pathParam(paramName, id)
                .contentType(ContentType.JSON)
                .body(luhu)
                .when()
                .put("/dog/{id}")
                .then()
                .statusCode(200)
                .body("name", equalTo(luhu.getName()))
                .body("height", equalTo(luhu.getHeight()))
                .body("weight", equalTo(luhu.getWeight()));
    }

//    @DataProvider(name = "dogsIdToDelete")
//    public Object[][] shouldSetIdForDogToBeDeleted() {
//        return new Object[][]{
//                {"id", 1}
//        };
//    }

    @Test(dataProvider = "firstId")
    public void shouldDeleteDogById(String paramName, int id) {
        saveDog();
        given()
                .contentType(ContentType.JSON)
                .when()
                .post("/dog");
        given()
                .pathParam(paramName, id)
                .when()
                .delete("/dog/{id}")
                .then()
                .statusCode(200);
    }

    @DataProvider(name = "unbalancedDogToSave")
    public Object[][] unbalancedDog() {
        DogDto unbalancedDog = new DogDto();
        int height = positiveInteger();
        unbalancedDog.setName(unicode(2, 200));
        unbalancedDog.setHeight(height);
        unbalancedDog.setWeight(height);
        return new Object[][]{
                {"dog", unbalancedDog}
        };
    }

    @Test(dataProvider = "unbalancedDogToSave")
    public void failsUnbalancedDogSave(String paramName, DogDto unbalancedDog) {
        given()
                .contentType(ContentType.JSON)
                .body(unbalancedDog)
                .when()
                .post("/dog")
                .then()
                .statusCode(500);
    }

    @Test(dataProvider = "firstId")
    public void failsUpdateWithUnbalancedDog(String paramName, int id) {
        saveDog();
        DogDto dogDto = new DogDto();
        int height = positiveInteger();
        dogDto.setName(unicode(2, 200));
        dogDto.setHeight(height);
        dogDto.setWeight(height);
        given()
                .pathParam(paramName, id)
                .contentType(ContentType.JSON)
                .body(dogDto)
                .when()
                .put("/dog/{id}")
                .then()
                .statusCode(500);
    }

    private DogDto saveDog() {
        DogDto newDog = DogsHandler.setRandomDogDto();
        given()
                .contentType(ContentType.JSON)
                .body(newDog)
                .when()
                .post("/dog");
        return newDog;
    }

}
