package com.epam.dog.controller;

import com.epam.dog.DogsHandler;
import com.epam.dog.controller.vo.DogDto;
import com.epam.dog.dao.DogDAO;
import com.epam.dog.dao.HibernateDao;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ContextConfiguration("file:src/main/webapp/WEB-INF/mvc-dispatcher-servlet.xml")
public class DogCtrlMockTest {

    private MockMvc mvc;
    public DogDAO dogDAO;

    @BeforeTest
    public void setup() {
        dogDAO = new HibernateDao();
        mvc = MockMvcBuilders.standaloneSetup(new DogController(dogDAO)).build();
    }

    private ResultActions saveDog(DogDto dog) throws Exception {
        final ObjectMapper mapper = new ObjectMapper();
        final String jsonDog = mapper.writeValueAsString(dog);

        return mvc.perform(post("/dog")
                .content(jsonDog)
                .contentType(MediaType.APPLICATION_JSON));

    }

    @Test
    public void shouldSaveDog() throws Exception {
        DogDto dogDto = DogsHandler.setRandomDogDto();
        saveDog(dogDto)
                .andExpect(status().isCreated());

    }

    @Test
    public void shouldGetDogById() throws Exception {
        DogDto dogDto = DogsHandler.setRandomDogDto();
        ResultActions resultActions = saveDog(dogDto);
        mvc.perform(get("/dog/3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(dogDto.getName()))
                .andExpect(jsonPath("$.height").value(dogDto.getHeight()))
                .andExpect(jsonPath("$.weight").value(dogDto.getWeight()));

    }

    @Test
    public void shouldReturnAllDogs() {
        //TODO
    }

    @Test
    public void shouldUpdateDog() throws Exception {
        DogDto dogDto = DogsHandler.setRandomDogDto();
        saveDog(dogDto);
        DogDto updatedDog = DogsHandler.setRandomDogDto();
        //TODO
    }

    @Test
    public void shouldDeleteDogById() {
        //TODO
    }

}