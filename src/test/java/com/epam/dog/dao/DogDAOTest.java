package com.epam.dog.dao;

import com.epam.dog.DogsHandler;
import com.epam.dog.vo.Dog;
import com.epam.dog.vo.DogDto;
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
        DogDto randomDog = DogsHandler.setRandomDogDto();
        Dog expectedDog = new Dog();
        expectedDog.setId(1);
        expectedDog.setName(randomDog.getName());
        expectedDog.setHeight(randomDog.getHeight());
        expectedDog.setWeight(randomDog.getWeight());
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
        DogDto newDog = DogsHandler.setRandomDogDto();
//        int id = inMemoryDao.getAllDogs().size() + 1;
//        newDog.setId(id);

        int id = inMemoryDao.saveDog(newDog.getName(), newDog.getHeight(), newDog.getWeight());
        Dog dog = new Dog();
        dog.setId(id);
        dog.setName(newDog.getName());
        dog.setHeight(newDog.getHeight());
        dog.setWeight(newDog.getWeight());
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
        DogDto dog = DogsHandler.setRandomDogDto();

        int id = inMemoryDao.saveDog(dog.getName(), dog.getHeight(), dog.getWeight());
        Dog expectedDog = new Dog();
        expectedDog.setId(id);
        expectedDog.setName(dog.getName());
        expectedDog.setHeight(dog.getHeight());
        expectedDog.setWeight(dog.getWeight());
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
