package com.epam.dog.controller;

import com.epam.dog.DogsHandler;
import com.epam.dog.controller.vo.Dog;
import com.epam.dog.controller.vo.DogDto;
import com.epam.dog.dao.DogDAO;
import com.epam.dog.dao.HibernateDao;
import io.restassured.http.ContentType;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.core.IsEqual.equalTo;


public class DogControllerTest {
    private Map<Integer, Dog> dogsMap;
    private DogDAO dogDAO;

    @BeforeTest
    public void shouldSetDogsInitialData() {
//        dogDAO = new InMemoryDao();
        dogDAO = new HibernateDao();
        dogsMap = dogDAO.getAllDogs();
    }



    @DataProvider(name = "firstId")
    public static Object[][] shouldSetFirstDogsId() {
        return new Object[][]{
                {"id", 2}
        };
    }

    @Test(dataProvider = "firstId")
    public void shouldGetDogById(String paramName, int id) {
        given().
                pathParam(paramName, id).
                when().
                get("/dog/{id}").
                then().
                assertThat()
                .statusCode(200)
                .body("name", equalTo(dogsMap.get(id).getName()))
                .body("height", equalTo(dogsMap.get(id).getHeight()))
                .body("weight", equalTo(dogsMap.get(id).getWeight()));
    }

    @Test()
    public void shouldGetAllDogs() {
        for (Dog dog : dogDAO.getAllDogs().values()) {
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
                {"id", 3}
        };
    }

    @Test(dataProvider = "dogsIdToDelete")
    public void shouldDeleteDogById(String paramName, int id) {
        DogDto dog = DogsHandler.setRandomDogDto();

        given()
                .contentType(ContentType.JSON)
                .body(dog)
                .when()
                .post("/dog");

//        Dog doge = dogsMap.get(dogsMap.size());

        given()
                .pathParam(paramName, id)
                .when()
                .delete("/dog/{id}")
                .then()
                .statusCode(200);
    }

}
