package com.epam.dog.dao;

import com.epam.dog.controller.vo.Dog;
import com.epam.dog.controller.vo.DogDto;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.Assert.*;

public class DogDAOTest {
    private Map<Integer, Dog> dogExpectedMap;
    private InMemoryDao inMemoryDao = new InMemoryDao();

    @BeforeSuite
    public void init() {
        dogExpectedMap = new ConcurrentHashMap<>();
        Dog expectedDog = new Dog(1, "Aqua", 45, 10);
        dogExpectedMap.put(expectedDog.getId(), expectedDog);
    }

    @Test
    public void shouldReturnAllDogs() {

        Map<Integer, Dog> dogActualMap = inMemoryDao.getAllDogs();
        assertEquals(1, dogActualMap.size());
        assertEquals(dogExpectedMap.get(1).getName(), dogActualMap.get(1).getName());
        assertEquals(dogExpectedMap.get(1).getHeight(), dogActualMap.get(1).getHeight());
        assertEquals(dogExpectedMap.get(1).getWeight(), dogActualMap.get(1).getWeight());
    }

    @Test
    public void shouldSaveSpecifiedDog() {
        int sizeBefore = inMemoryDao.getAllDogs().size();
        DogDto newDog = new DogDto();
//        int id = inMemoryDao.getAllDogs().size() + 1;
//        newDog.setId(id);
        newDog.setName("Shiny");
        newDog.setHeight(70);
        newDog.setWeight(35);
        int id = inMemoryDao.saveDog(newDog);
        Dog dog = new Dog(id, newDog.getName(), newDog.getHeight(), newDog.getWeight());
        dogExpectedMap.put(id, dog);
        System.out.println(id);
        assertEquals(sizeBefore + 1, inMemoryDao.getAllDogs().size());
        assertTrue(inMemoryDao.getAllDogs().containsKey(id));
//        assertTrue(inMemoryDao.getAllDogs().containsValue(dog));
        Dog expectedDog = dogExpectedMap.get(dog.getId());
        Dog actualDog = inMemoryDao.getAllDogs().get(dog.getId());
        assertEquals(expectedDog.getId(), actualDog.getId());
        assertEquals(expectedDog.getName(), actualDog.getName());
        assertEquals(expectedDog.getHeight(), actualDog.getHeight());
        assertEquals(expectedDog.getWeight(), actualDog.getWeight());
    }

    @Test
    public void shouldReturnDogById() {
        DogDto dog = new DogDto();
//        int id = inMemoryDao.getAllDogs().size() + 1;
//        dog.setId(id);
        dog.setName("Tiny");
        dog.setHeight(20);
        dog.setWeight(5);

        int id = inMemoryDao.saveDog(dog);
        Dog expectedDog = new Dog(id, dog.getName(), dog.getHeight(), dog.getWeight());
        Dog actualDog = inMemoryDao.getDogById(id);
        assertEquals(expectedDog.getId(), actualDog.getId());
        assertEquals(expectedDog.getName(), actualDog.getName());
        assertEquals(expectedDog.getHeight(), actualDog.getHeight());
        assertEquals(expectedDog.getWeight(), actualDog.getWeight());
    }

    @Test
    public void shouldRemoveDogById() {
        shouldRemoveDogById(2);
        shouldRemoveDogById(3);
    }

    private void shouldRemoveDogById(int id) {
        int sizeBefore = inMemoryDao.getAllDogs().size();
        InMemoryDao stateBefore = inMemoryDao;
        if (inMemoryDao.hasDog(id)) {
            inMemoryDao.freeDogById(id);
            assertEquals(sizeBefore - 1, inMemoryDao.getAllDogs().size());
            assertFalse(inMemoryDao.getAllDogs().containsKey(id));
        } else {
            assertEquals(stateBefore, inMemoryDao);
        }
    }

}
