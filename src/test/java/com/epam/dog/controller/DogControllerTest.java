package com.epam.dog.controller;

import com.epam.dog.DogsHandler;
import com.epam.dog.controller.vo.Dog;
import com.epam.dog.controller.vo.DogDto;
import com.epam.dog.dao.DogDAO;
import com.epam.dog.dao.InMemoryDao;
import io.restassured.http.ContentType;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.core.IsEqual.equalTo;


public class DogControllerTest {
    private Map<Integer, Dog> dogsMap;

    @BeforeSuite
    public void shouldSetDogsInitialData() {
        DogDAO dogDAO = new InMemoryDao();
        dogsMap = dogDAO.getAllDogs();
    }



    @DataProvider(name = "firstId")
    public static Object[][] shouldSetFirstDogsId() {
        return new Object[][]{
                {"id", 1}
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
//                .contentType(ContentType.JSON)
                .statusCode(200)
                .body("name", equalTo(dogsMap.get(id).getName()))
                .body("height", equalTo(dogsMap.get(id).getHeight()))
                .body("weight", equalTo(dogsMap.get(id).getWeight()));
    }

    @Test()
    public void shouldGetAllDogs() {
        ArrayList<String> dogNames = new ArrayList<>();
        ArrayList<Integer> dogHeights = new ArrayList<>();
        ArrayList<Integer> dogWeights = new ArrayList<>();
        for (Dog dog : dogsMap.values()) {
            dogNames.add(dog.getName());
            dogHeights.add(dog.getHeight());
            dogWeights.add(dog.getWeight());
        }

        given().
                when().
                get("/dog").
                then().
                assertThat()
                .contentType(ContentType.JSON)
                .statusCode(200)
                .body("name", contains(dogNames.get(0)))
                .body("height", contains(dogHeights.get(0)))
                .body("weight", contains(dogWeights.get(0)));
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
