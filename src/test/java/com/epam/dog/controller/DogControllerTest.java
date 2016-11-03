package com.epam.dog.controller;

import com.epam.dog.DogsHandler;
import com.epam.dog.vo.DogDto;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import static io.qala.datagen.RandomShortApi.positiveInteger;
import static io.qala.datagen.RandomShortApi.unicode;
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
                .body("weight", equalTo(newDog.getWeight()));
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
                .body("name", hasItem(secondDog.getName()))
                .body("height", hasItem(secondDog.getHeight()))
                .body("weight", hasItem(secondDog.getWeight()));
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
        int id = saveDog(DogsHandler.setRandomDogDto());
        DogDto luhu = DogsHandler.setCorrectDogDto();
        given()
                .pathParam("id", id)
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

    @Test
    public void shouldDeleteDogById() {
        int id = saveDog(DogsHandler.setRandomDogDto());
        given()
                .contentType(ContentType.JSON)
                .when()
                .post("/dog");
        given()
                .pathParam("id", id)
                .when()
                .delete("/dog/{id}")
                .then()
                .statusCode(200);
    }

//    @Test
//    public void failsUnbalancedDogSave() {
//        DogDto unbalancedDog = new DogDto();
//        int height = positiveInteger();
//        unbalancedDog.setName(unicode(2, 200));
//        unbalancedDog.setHeight(height);
//        unbalancedDog.setWeight(height);
//        given()
//                .contentType(ContentType.JSON)
//                .body(unbalancedDog)
//                .when()
//                .post("/dog")
//                .then()
//                .statusCode(500);
//    }
//
//    @Test
//    public void failsUpdateWithUnbalancedDog() {
//        int id = saveDog(DogsHandler.setRandomDogDto());
//        DogDto dogDto = new DogDto();
//        int height = positiveInteger();
//        dogDto.setName(unicode(2, 200));
//        dogDto.setHeight(height);
//        dogDto.setWeight(height);
//        given()
//                .pathParam("id", id)
//                .contentType(ContentType.JSON)
//                .body(dogDto)
//                .when()
//                .put("/dog/{id}")
//                .then()
//                .statusCode(500);
//    }

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
