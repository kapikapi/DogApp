package com.epam.dog.controller;

import com.epam.dog.DogsHandler;
import com.epam.dog.vo.DogDto;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.core.IsEqual.equalTo;

public class DogControllerTest{

    @Test
    public void shouldGetDogById() {
        DogDto newDog = DogsHandler.setCorrectDogDto();
        int id = saveDog(newDog);
        given().
                pathParam("id", id).
                when().
                get("/dog/{id}").
                then().
                assertThat()
                .statusCode(200)
                .body("name", equalTo(newDog.getName()))
                .body("height", equalTo(newDog.getHeight()))
                .body("weight", equalTo(newDog.getWeight()))
                .body("dateOfBirth", equalTo(DogsHandler.localDateToListFormat(newDog.getDateOfBirth())));
    }

    @Test
    public void shouldGetAllDogs() {
        DogDto firstDog = DogsHandler.setCorrectDogDto();
        DogDto secondDog = DogsHandler.setCorrectDogDto();
        saveDog(firstDog);
        saveDog(secondDog);
        given().
                when().
                get("/dog").
                then().
                assertThat()
                .contentType(ContentType.JSON)
                .statusCode(200)
                .body("name", hasItem(firstDog.getName()))
                .body("height", hasItem(firstDog.getHeight()))
                .body("weight", hasItem(firstDog.getWeight()))
                .body("dateOfBirth", hasItem(DogsHandler.localDateToListFormat(firstDog.getDateOfBirth())))
                .body("name", hasItem(secondDog.getName()))
                .body("height", hasItem(secondDog.getHeight()))
                .body("weight", hasItem(secondDog.getWeight()))
                .body("dateOfBirth", hasItem(DogsHandler.localDateToListFormat(secondDog.getDateOfBirth())));
    }

    @Test
    public void shouldSaveSpecifiedDog() {
        DogDto newDog = DogsHandler.setCorrectDogDto();
        given()
                .contentType(ContentType.JSON)
                .body(newDog)
                .when()
                .post("/dog")
                .then()
                .statusCode(201);
    }


    @Test
    public void shouldUpdateDogById() {
        DogDto dogDto = DogsHandler.setCorrectDogDto();
        int id = saveDog(dogDto);

        given()
                .pathParam("id", id)
                .contentType(ContentType.JSON)
                .body(dogDto)
                .when()
                .put("/dog/{id}")
                .then()
                .statusCode(200)
                .body("name", equalTo(dogDto.getName()))
                .body("height", equalTo(dogDto.getHeight()))
                .body("weight", equalTo(dogDto.getWeight()))
                .body("dateOfBirth", equalTo(DogsHandler.localDateToListFormat(dogDto.getDateOfBirth())));
    }

    @Test
    public void shouldDeleteDogById() {
        int id = saveDog(DogsHandler.setCorrectDogDto());
        given()
                .pathParam("id", id)
                .when()
                .delete("/dog/{id}")
                .then()
                .statusCode(200);

        given().
                pathParam("id", id).
                when().
                get("/dog/{id}").
                then().
                assertThat()
                .statusCode(404);
    }

    private int saveDog(DogDto newDog) {
        Response response = given()
                .contentType(ContentType.JSON)
                .body(newDog)
                .when()
                .post("/dog").andReturn();
        String id = response.getHeader("Location").substring(response.getHeader("Location").lastIndexOf("/") + 1);
        return Integer.parseInt(id);
    }
}
