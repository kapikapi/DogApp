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
        saveDog(firstDog);
        saveDog(secondDog);
        mvc.perform(get("/dog")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value(firstDog.getName()))
                .andExpect(jsonPath("$[0].height").value(firstDog.getHeight()))
                .andExpect(jsonPath("$[0].weight").value(firstDog.getWeight()))
                .andExpect(jsonPath("$[1].name").value(secondDog.getName()))
                .andExpect(jsonPath("$[1].height").value(secondDog.getHeight()))
                .andExpect(jsonPath("$[1].weight").value(secondDog.getWeight()));
    }

    @Test
    public void shouldUpdateDog() throws Exception {
        DogDto dogDto = DogsHandler.setCorrectDogDto();
        int id = saveDog(dogDto);
        DogDto updatedDog = DogsHandler.setCorrectDogDto();
        final String jsonDog = dogDtoToJson(updatedDog);

        mvc.perform(put("/dog/" + id)
                .content(jsonDog)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(updatedDog.getName()))
                .andExpect(jsonPath("$.height").value(updatedDog.getHeight()))
                .andExpect(jsonPath("$.weight").value(updatedDog.getWeight()));
    }

    @Test
    public void shouldDeleteDogById() throws Exception {
        DogDto dogDto = DogsHandler.setCorrectDogDto();
        int id = saveDog(dogDto);
        mvc.perform(delete("/dog/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(dogDto.getName()))
                .andExpect(jsonPath("$.height").value(dogDto.getHeight()))
                .andExpect(jsonPath("$.weight").value(dogDto.getWeight()));
    }

    private int saveDog(DogDto dog) throws Exception {
        final String jsonDog = dogDtoToJson(dog);

        MvcResult result = mvc.perform(post("/dog")
                .content(jsonDog)
                .contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        String headerLocation = result.getResponse().getHeader("Location");
        String id = headerLocation.substring(headerLocation.lastIndexOf("/") + 1);
        return Integer.parseInt(id);
    }

    private String dogDtoToJson(DogDto dog) throws JsonProcessingException {
        final ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(dog);
    }
}