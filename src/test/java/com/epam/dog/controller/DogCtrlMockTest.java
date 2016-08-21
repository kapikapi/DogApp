package com.epam.dog.controller;

import com.epam.dog.controller.vo.Dog;
import com.epam.dog.dao.InMemoryDao;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ContextConfiguration("file:src/main/webapp/WEB-INF/mvc-dispatcher-servlet.xml")
public class DogCtrlMockTest {

    private MockMvc mvc;
    @Mock
    public InMemoryDao dogDAO;

    @BeforeSuite
    public void setup() {
        MockitoAnnotations.initMocks(this);
        mvc = MockMvcBuilders.standaloneSetup(new DogController(dogDAO)).build();
        System.out.println(mvc);
    }

    private ResultActions saveDog(Dog dog) throws Exception {
        final ObjectMapper mapper = new ObjectMapper();
        final String jsonDog = mapper.writeValueAsString(dog);
        return mvc.perform(post("/dog")
                .content(jsonDog)
                .contentType(MediaType.APPLICATION_JSON));

    }

    @Test
    public void shouldSaveDog() throws Exception {
        Dog newDog = new Dog(1, "Fabulous", 50, 15);

        saveDog(newDog)
                .andExpect(status().isCreated());

    }

    @Test
    public void shouldGetDogById() throws Exception {
//        Dog newDog = new Dog(2, "Doge", 55, 13);

        mvc.perform(get("/dog/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Doge"))
                .andExpect(jsonPath("$.height").value(55))
                .andExpect(jsonPath("$.weight").value(13));

    }

    @Test
    public void shouldReturnAllDogs() {
        //TODO
    }

    @Test
    public void shouldUpdateDog() {
        //TODO
    }

    @Test
    public void shouldDeleteDogById() {
        //TODO
    }

}