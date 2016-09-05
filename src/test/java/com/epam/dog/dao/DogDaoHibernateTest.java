package com.epam.dog.dao;

import com.epam.dog.DogsHandler;
import com.epam.dog.controller.vo.Dog;
import com.epam.dog.controller.vo.DogDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.testng.annotations.Test;

import java.util.Map;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;
import static org.unitils.reflectionassert.ReflectionAssert.assertReflectionEquals;

@ContextConfiguration(locations = {"classpath:../webapp/WEB-INF/mvc-dispatcher-servlet.xml",
"classpath:../mvc-dispatcher-servlet-test.xml"})
public class DogDaoHibernateTest {

    @Autowired
    DogDAO dogDAO = new HibernateDao();

    private Dog saveNewDog() {
        DogDto newDog = DogsHandler.setRandomDogDto();
        int id = dogDAO.saveDog(newDog.getName(), newDog.getHeight(), newDog.getWeight());
        Dog dog = new Dog();
        dog.setId(id);
        dog.setName(newDog.getName());
        dog.setHeight(newDog.getHeight());
        dog.setWeight(newDog.getWeight());
        return dog;
    }

    @Test
    public void shouldReturnAllDogs() {
        Dog dog = saveNewDog();
        Map<Integer, Dog> dogActualMap = dogDAO.getAllDogs();
//        assertEquals(1, dogActualMap.size());
        assertReflectionEquals(dog, dogActualMap.get(dog.getId()));
//        assertEquals(dog.getName(), dogActualMap.get(dog.getId()).getName());
//        assertEquals(dog.getHeight(), dogActualMap.get(dog.getId()).getHeight());
//        assertEquals(dog.getWeight(), dogActualMap.get(dog.getId()).getWeight());
    }

    @Test
    public void shouldSaveSpecifiedDog() {
        int sizeBefore = dogDAO.getAllDogs().size();
        Dog dog = saveNewDog();

        assertEquals(sizeBefore + 1, dogDAO.getAllDogs().size());
        assertTrue(dogDAO.getAllDogs().containsKey(dog.getId()));
        Dog actualDog = dogDAO.getAllDogs().get(dog.getId());
        assertReflectionEquals(dog, actualDog);
//        assertEquals(dog.getId(), actualDog.getId());
//        assertEquals(dog.getName(), actualDog.getName());
//        assertEquals(dog.getHeight(), actualDog.getHeight());
//        assertEquals(dog.getWeight(), actualDog.getWeight());
    }

    @org.testng.annotations.Test
    public void shouldReturnDogById() {
//        DogDto dogDto = DogsHandler.setRandomDogDto();
//        int id = dogDAO.saveDog(dogDto.getName(), dogDto.getHeight(), dogDto.getWeight());
        Dog dog = saveNewDog();
        Dog actualDog = dogDAO.getDogById(dog.getId());
        assertReflectionEquals(dog, actualDog);
//        assertEquals(id, actualDog.getId());
//        assertEquals(dogDto.getName(), actualDog.getName());
//        assertEquals(dogDto.getHeight(), actualDog.getHeight());
//        assertEquals(dogDto.getWeight(), actualDog.getWeight());
    }

    @org.testng.annotations.Test
    public void shouldRemoveDogById() {
        Dog dog = saveNewDog();
        int sizeBefore = dogDAO.getAllDogs().size();
        dogDAO.freeDogById(dog.getId());
        assertEquals(sizeBefore - 1, dogDAO.getAllDogs().size());
        assertFalse(dogDAO.getAllDogs().containsKey(dog.getId()));

    }
}