package com.epam.dog.controller;

import com.epam.dog.controller.vo.Dog;
import com.epam.dog.dao.DogHandler;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.testng.annotations.BeforeSuite;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
@WebAppConfiguration
public class DogCtrlMockTest {

    private MockMvc mvc;
    @Mock
    public DogHandler dogDAO;

    @BeforeSuite
    public void setup() {
        MockitoAnnotations.initMocks(this);
        mvc = MockMvcBuilders.standaloneSetup(new DogController(dogDAO)).build();
    }

    @Test
    public void shouldSaveDog() {
        Dog newDog = new Dog();
        int id = 1;
        newDog.setId(id);
        newDog.setName("Fabulous");
        newDog.setHeight(50);
        newDog.setWeight(15);

        when(dogDAO.saveDog(newDog)).thenReturn(id);

        try {
            mvc.perform(post("/dog").contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isCreated());
        } catch (Exception e) {
            e.printStackTrace();
        }
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
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.2.name").value("Doge"))
                    .andExpect(jsonPath("$.2.height").value("55"))
                    .andExpect(jsonPath("$.2.weight").value("13"));
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
