package com.epam.dog.controller;

import com.epam.dog.controller.vo.Dog;
import com.epam.dog.dao.DogDAO;
import com.epam.dog.dao.DogHandler;
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
//    private DogController dogController;

    @BeforeSuite
    public void shouldSetDogsInitialData() {
        DogDAO dogDAO = new DogHandler();
//        dogController = new DogController();
        dogsMap = dogDAO.getAllDogs();
    }

    @DataProvider(name = "firstId")
    public static Object[][] shouldSetFirstDogsId() {
        return new Object[][]{
                {"id", 1}
        };
    }

    @Test(dataProvider = "shouldSetFirstDogsId")
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
        Dog hamilton = new Dog();
        hamilton.setId(2);
        hamilton.setName("Hamilton");
        hamilton.setHeight(50);
        hamilton.setWeight(15);
        return new Object[][]{
                {"dog", hamilton}
        };
    }

    @Test(dataProvider = "shouldSetNewDogData")
    public void shouldSaveSpecifiedDog(String paramName, Dog newDog) {
        given()
                .contentType(ContentType.JSON)
                .body(newDog)
                .when()
                .post("/dog")
                .then()
                .statusCode(201);
    }

    @DataProvider(name = "dogToDelete")
    public Object[][] shouldSetIdForDogToBeDeleted() {
        return new Object[][]{
                {"id", 2}
        };
    }

    @Test(dataProvider = "shouldSetIdForDogToBeDeleted")
    public void shouldDeleteDogById(String paramName, int id) {
        given()
                .pathParam(paramName, id)
                .when()
                .delete("/dog/{id}")
                .then()
                .statusCode(200);

    }

    @Test(dataProvider = "shouldSetFirstDogsId")
    public void shouldUpdateDogById(String paramName, int id) {
        Dog luhu = new Dog();
        luhu.setId(id);
        luhu.setName("Luhu");
        luhu.setHeight(30);
        luhu.setWeight(10);
        given()
                .pathParam(paramName, id)
                .contentType(ContentType.JSON)
                .body(luhu)
                .when()
                .put("/dog/{id}")
                .then()
                .statusCode(200)
                .body("name", equalTo("Luhu"))
                .body("height", equalTo(30))
                .body("weight", equalTo(10));
    }

}
