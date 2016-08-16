package com.epam.dog.dao;

import com.epam.dog.controller.vo.Dog;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class DogDAOTest {
    private Map<Integer, Dog> dogExpectedMap;
    private DogHandler dogHandler = new DogHandler();

    @Before
    public void init() {
        dogExpectedMap = new HashMap<>();
        Dog expectedDog = new Dog();
        expectedDog.setId(1);
        expectedDog.setName("Aqua");
        expectedDog.setHeight(45);
        expectedDog.setWeight(10);
        dogExpectedMap.put(expectedDog.getId(), expectedDog);
    }

    @Test
    public void shouldReturnAllDogs() {

        Map<Integer, Dog> dogActualMap = dogHandler.getAllDogs();
        assertEquals(1, dogActualMap.size());
        assertEquals(dogExpectedMap.get(1).getName(), dogActualMap.get(1).getName());
        assertEquals(dogExpectedMap.get(1).getHeight(), dogActualMap.get(1).getHeight());
        assertEquals(dogExpectedMap.get(1).getWeight(), dogActualMap.get(1).getWeight());
    }

    @Test
    public void shouldSaveSpecifiedDog() {
        int sizeBefore = dogHandler.getAllDogs().size();
        Dog newDog = new Dog();
        int id = dogHandler.getAllDogs().size() + 1;
        newDog.setId(id);
        newDog.setName("Shiny");
        newDog.setHeight(70);
        newDog.setWeight(35);
        dogExpectedMap.put(newDog.getId(), newDog);
        dogHandler.saveDog(newDog);
        assertEquals(sizeBefore + 1, dogHandler.getAllDogs().size());
        assertTrue(dogHandler.getAllDogs().containsKey(id));
        assertTrue(dogHandler.getAllDogs().containsValue(newDog));
        assertEquals(dogExpectedMap.get(id), dogHandler.getAllDogs().get(id));

    }

    @Test
    public void shouldReturnDogById() {
        Dog expectedDog = new Dog();
        int id = dogHandler.getAllDogs().size() + 1;
        expectedDog.setId(id);
        expectedDog.setName("Tiny");
        expectedDog.setHeight(20);
        expectedDog.setWeight(5);
        dogHandler.saveDog(expectedDog);
        Dog actualDog = dogHandler.getDogById(id);
        assertEquals(expectedDog, actualDog);
    }

    @Test
    public void shouldRemoveDogById() {
        shouldRemoveDogById(2);
        shouldRemoveDogById(3);
    }

    private void shouldRemoveDogById(int id) {
        int sizeBefore = dogHandler.getAllDogs().size();
        DogHandler stateBefore = dogHandler;
        if (dogHandler.hasDog(id)) {
            dogHandler.freeDogById(id);
            assertEquals(sizeBefore - 1, dogHandler.getAllDogs().size());
            assertFalse(dogHandler.getAllDogs().containsKey(id));
        } else {
            assertEquals(stateBefore, dogHandler);
        }
    }

    @Test
    public void shouldReturnIfDogExistsById() {
        Dog lucky = new Dog();
        int id = dogHandler.getAllDogs().size() + 1;
        lucky.setId(id);
        lucky.setName("Lucky");
        lucky.setHeight(30);
        lucky.setWeight(10);
        dogHandler.saveDog(lucky);
        assertTrue(dogHandler.hasDog(id));
        assertFalse(dogHandler.hasDog(id + 1));
    }

}
