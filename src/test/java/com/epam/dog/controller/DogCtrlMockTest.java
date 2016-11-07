package com.epam.dog.controller;

import com.epam.dog.DogsHandler;
import com.epam.dog.vo.DogDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.qala.datagen.RandomShortApi.unicode;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/mvc-dispatcher-servlet.xml"})
public class DogCtrlMockTest extends AbstractTestNGSpringContextTests {

    private MockMvc mvc;

    @Autowired
    public DogController dogController;

    @BeforeClass
    public void setup() {
        mvc = MockMvcBuilders.standaloneSetup(dogController).build();
    }

    @Test
    public void shouldSaveDogAndGetById() throws Exception {
        DogDto dogDto = DogsHandler.setCorrectDogDto();
        int id = saveDog(dogDto);

        mvc.perform(get("/dog/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(dogDto.getName()))
                .andExpect(jsonPath("$.height").value(dogDto.getHeight()))
                .andExpect(jsonPath("$.weight").value(dogDto.getWeight()));
    }

    @Test
    public void shouldReturnAllDogs() throws Exception {
        DogDto firstDog = DogsHandler.setCorrectDogDto();
        DogDto secondDog = DogsHandler.setCorrectDogDto();
        int firstId = saveDog(firstDog);
        int secondId = saveDog(secondDog);
        String jsonPathPattern = "$[?(@.id==%s)].%s";
        mvc.perform(get("/dog")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath(String.format(jsonPathPattern, firstId, "name")).value(firstDog.getName()))
                .andExpect(jsonPath(String.format(jsonPathPattern, firstId, "height")).value(firstDog.getHeight()))
                .andExpect(jsonPath(String.format(jsonPathPattern, firstId, "weight")).value(firstDog.getWeight()))
                .andExpect(jsonPath(String.format(jsonPathPattern, firstId, "dateOfBirth")).value(firstDog.getDateOfBirth()))
                .andExpect(jsonPath(String.format(jsonPathPattern, secondId, "name")).value(secondDog.getName()))
                .andExpect(jsonPath(String.format(jsonPathPattern, secondId, "height")).value(secondDog.getHeight()))
                .andExpect(jsonPath(String.format(jsonPathPattern, secondId, "weight")).value(secondDog.getWeight()))
                .andExpect(jsonPath(String.format(jsonPathPattern, secondId, "dateOfBirth")).value(secondDog.getDateOfBirth()));
        ;
    }

    @Test
    public void shouldUpdateDog() throws Exception {
        DogDto dogDto = DogsHandler.setCorrectDogDto();
        int id = saveDog(dogDto);
        DogDto updatedDog = DogsHandler.setCorrectDogDto();
        final String jsonDog = objectToJson(updatedDog);
        mvc.perform(put("/dog/" + id)
                .content(jsonDog)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(updatedDog.getName()))
                .andExpect(jsonPath("$.height").value(updatedDog.getHeight()))
                .andExpect(jsonPath("$.weight").value(updatedDog.getWeight()))
                .andExpect(jsonPath("$.dateOfBirth").value(updatedDog.getDateOfBirth()));
    }

    @Test
    public void shouldDeleteDogById() throws Exception {
        int id = saveDog(DogsHandler.setCorrectDogDto());
        mvc.perform(delete("/dog/" + id))
                .andExpect(status().isOk());
        mvc.perform(get("/dog/" + id))
                .andExpect(status().isNotFound());
    }

    @Test
    public void tooLongName_failSave() throws Exception {
        DogDto dogDto = DogsHandler.setTooLongNameDogDto();
        checksBadRequest(dogDto);
    }

    @Test
    public void tooShortDogName_failSave() throws Exception {
        DogDto dogDto = DogsHandler.setCorrectDogDto();
        dogDto.setName("");
        checksBadRequest(dogDto);
    }

    @Test
    public void unbalancedDog_failSave() throws Exception {
        DogDto dogDto = DogsHandler.setUnbalancedDogDto();
        checksBadRequest(dogDto);
    }

    @Test
    public void wrongBorderDogName_failSave() throws Exception {
        DogDto dogDto = DogsHandler.setCorrectDogDto();
        dogDto.setName(unicode(101));
        checksBadRequest(dogDto);
    }

    @Test
    public void correctBorderName_succeedSave() throws Exception {
        DogDto dogDto = DogsHandler.setCorrectDogDto();
        dogDto.setName(unicode(1));
        int id = saveDog(dogDto);
        mvc.perform(get("/dog/" + id))
                .andExpect(status().isOk());
        dogDto.setName(unicode(100));
        id = saveDog(dogDto);
        mvc.perform(get("/dog/" + id))
                .andExpect(status().isOk());
    }

    private void checksBadRequest(DogDto dogDto) throws Exception {
        mvc.perform(post("/dog")
                .content(objectToJson(dogDto))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    private int saveDog(DogDto dog) throws Exception {
        final String jsonDog = objectToJson(dog);
        MvcResult result = mvc.perform(post("/dog")
                .content(jsonDog)
                .contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        String headerLocation = result.getResponse().getHeader("Location");
        String id = headerLocation.substring(headerLocation.lastIndexOf("/") + 1);
        return Integer.parseInt(id);
    }

    private String objectToJson(Object dog) throws JsonProcessingException {
        final ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(dog);
    }

}