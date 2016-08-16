package com.epam.dog.controller;

import com.epam.dog.controller.vo.Dog;
import com.epam.dog.dao.DogHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@ContextConfiguration("file:src/main/webapp/WEB-INF/mvc-dispatcher-servlet.xml")
public class DogCtrlMockTest {

    private MockMvc mvc;
    @Mock
    public DogHandler dogDAO;

    @BeforeSuite
    public void setup() {
        MockitoAnnotations.initMocks(this);
        mvc = MockMvcBuilders.standaloneSetup(new DogController(dogDAO)).build();
        System.out.println(mvc);
    }

    @Test
    public void shouldSaveDog() throws Exception {
        Dog newDog = new Dog();
        int id = 1;
        newDog.setId(id);
        newDog.setName("Fabulous");
        newDog.setHeight(50);
        newDog.setWeight(15);

        final ObjectMapper mapper = new ObjectMapper();
        final String jsonDog = mapper.writeValueAsString(newDog);

        when(dogDAO.saveDog(newDog)).thenReturn(id);

        mvc.perform(post("/dog")
                .content(jsonDog)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

    }

    @Test
    public void shouldGetDogById() {
        Dog newDog = new Dog();
        int id = 2;
        newDog.setId(id);
        newDog.setName("Doge");
        newDog.setHeight(55);
        newDog.setWeight(13);
        when(dogDAO.getDogById(id)).thenReturn(newDog);

        try {
            mvc.perform(get("/dog/2"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.name").value("Doge"))
                    .andExpect(jsonPath("$.height").value(55))
                    .andExpect(jsonPath("$.weight").value(13));
        } catch (Exception e) {
            e.printStackTrace();
        }
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
