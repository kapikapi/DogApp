package com.epam.dog.controller;

import com.epam.dog.controller.vo.Dog;
import com.epam.dog.controller.vo.DogDto;
import com.epam.dog.dao.HibernateDao;
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

import static io.qala.datagen.RandomShortApi.english;
import static io.qala.datagen.RandomShortApi.integer;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ContextConfiguration("file:src/main/webapp/WEB-INF/mvc-dispatcher-servlet.xml")
public class DogCtrlMockTest {

    private MockMvc mvc;
//    @Mock
//    public InMemoryDao dogDAO;
    @Mock
    public HibernateDao dogDAO;

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

    private DogDto setRandomDogDto() {

        DogDto dog = new DogDto();
        dog.setName(english(10));
        dog.setHeight(integer(20, 100));
        dog.setWeight(integer(3, 70));
        return dog;
    }

    @Test
    public void shouldSaveDog() throws Exception {
        DogDto dogDto = setRandomDogDto();
        Dog newDog = new Dog(1, dogDto.getName(), dogDto.getHeight(), dogDto.getWeight());

        saveDog(newDog)
                .andExpect(status().isCreated());

    }

    @Test
    public void shouldGetDogById() throws Exception {
        DogDto dogDto = setRandomDogDto();
        Dog newDog = new Dog(2, dogDto.getName(), dogDto.getHeight(), dogDto.getWeight());
        saveDog(newDog);
        mvc.perform(get("/dog/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(newDog.getName()))
                .andExpect(jsonPath("$.height").value(newDog.getHeight()))
                .andExpect(jsonPath("$.weight").value(newDog.getWeight()));

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