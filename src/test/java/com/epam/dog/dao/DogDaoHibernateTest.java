package com.epam.dog.dao;

import com.epam.dog.DogsHandler;
import com.epam.dog.vo.Dog;
import com.epam.dog.vo.DogDto;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.springframework.transaction.annotation.Transactional;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;
import static org.unitils.reflectionassert.ReflectionAssert.assertReflectionEquals;

@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/mvc-dispatcher-servlet.xml"})
public class DogDaoHibernateTest extends AbstractTransactionalTestNGSpringContextTests{

    @Autowired
    private DogDAO dogDAO;

    @Autowired
    private SessionFactory sessionFactory;

    private Dog saveNewDog() {
        DogDto newDog = DogsHandler.setCorrectDogDto();
        Dog savedDog = dogDAO.saveDog(newDog.getName(), newDog.getHeight(), newDog.getWeight());
        Dog dog = new Dog();
        dog.setId(savedDog.getId());
        dog.setName(savedDog.getName());
        dog.setHeight(savedDog.getHeight());
        dog.setWeight(savedDog.getWeight());
        return dog;
    }

    private Dog getDogFromListById(List<Dog> dogList, int id) {
        for (Dog dog : dogList) {
            if (dog.getId() == id) {
                return dog;
            }
        }
        return null;
    }

    @Transactional
    @Test
    public void shouldReturnAllDogs() {
        Dog dogFirst = saveNewDog();
        Dog dogSecond = saveNewDog();
        List<Dog> expectedList = new ArrayList<>();
        expectedList.add(dogFirst);
        expectedList.add(dogSecond);
        flushAndClear();
        List<Dog> dogActualList = dogDAO.getAllDogs();
        assertReflectionEquals(expectedList, dogActualList);
    }

    @Transactional
    @Test
    public void shouldSaveSpecifiedDog() {
        int sizeBefore = dogDAO.getAllDogs().size();
        Dog dog = saveNewDog();
        List<Dog> updatedList = dogDAO.getAllDogs();
        assertEquals(sizeBefore + 1, updatedList.size());
        assertReflectionEquals(dog, getDogFromListById(updatedList, dog.getId()));
    }

    @Transactional
    @Test
    public void shouldUpdateDog() {
        Dog dog = saveNewDog();
        flushAndClear();
        DogDto newDogDto = DogsHandler.setCorrectDogDto();
        dog.setName(newDogDto.getName());
        dog.setHeight(newDogDto.getHeight());
        dog.setWeight(newDogDto.getWeight());
        dogDAO.editDogById(dog.getId(), dog.getName(), dog.getHeight(), dog.getWeight());
        flushAndClear();
        assertReflectionEquals(dogDAO.getDogById(dog.getId()), dog);
    }

    @Transactional
    @Test
    public void shouldReturnDogById() {
        Dog dog = saveNewDog();
        flushAndClear();
        Dog actualDog = dogDAO.getDogById(dog.getId());
        assertReflectionEquals(dog, actualDog);
    }

    @Transactional
    @Test
    public void shouldRemoveDogById() {
        Dog dog = saveNewDog();
        flushAndClear();
        int sizeBefore = dogDAO.getAllDogs().size();
        dogDAO.freeDogById(dog.getId());
        assertEquals(sizeBefore - 1, dogDAO.getAllDogs().size());
        assertNull(dogDAO.getDogById(dog.getId()));

    }

    private void flushAndClear() {
        sessionFactory.getCurrentSession().flush();
        sessionFactory.getCurrentSession().clear();
    }

}