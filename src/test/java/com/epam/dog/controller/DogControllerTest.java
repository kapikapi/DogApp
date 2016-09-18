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

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.core.IsEqual.equalTo;

@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/mvc-dispatcher-servlet.xml"})
public class DogControllerTest  extends AbstractTransactionalTestNGSpringContextTests {

    @Autowired
    private DogDAO dogDAO;

    @DataProvider(name = "firstId")
    public static Object[][] shouldSetFirstDogsId() {
        return new Object[][]{
                {"id", 2}
        };
    }

    @Test(dataProvider = "firstId")
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

    @Test(dataProvider = "firstId")
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

    @DataProvider(name = "dogsIdToDelete")
    public Object[][] shouldSetIdForDogToBeDeleted() {
        return new Object[][]{
                {"id", 1}
        };
    }

    @Test(dataProvider = "dogsIdToDelete")
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
