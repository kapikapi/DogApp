package com.epam.dog.dao;

import com.epam.dog.DogsHandler;
import com.epam.dog.vo.Dog;
import com.epam.dog.vo.DogDto;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.testng.annotations.Test;

import java.util.List;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.unitils.reflectionassert.ReflectionAssert.assertReflectionEquals;

@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/mvc-dispatcher-servlet.xml",
"file:src/test/mvc-dispatcher-servlet-test.xml"})
public class DogDaoHibernateTest extends AbstractTransactionalTestNGSpringContextTests{

    @Autowired
    DogDAO dogDAO = new HibernateDao();

    @Autowired
    private SessionFactory sessionFactory;

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
        sessionFactory.getCurrentSession().clear();
        List<Dog> dogActualList = dogDAO.getAllDogs();
        assertReflectionEquals(dog, dogActualList.get(dog.getId() - 1));
    }

    @Test
    public void shouldSaveSpecifiedDog() {
        int sizeBefore = dogDAO.getAllDogs().size();
        Dog dog = saveNewDog();
        sessionFactory.getCurrentSession().clear();
        List<Dog> updatedList = dogDAO.getAllDogs();
        assertEquals(sizeBefore + 1, updatedList.size());
//        assertTrue(dogDAO.getAllDogs().contains(dog));
        Dog actualDog = updatedList.get(dog.getId() - 1);
        assertReflectionEquals(dog, actualDog);
    }

    @Test
    public void shouldReturnDogById() {
        Dog dog = saveNewDog();
        sessionFactory.getCurrentSession().clear();
        Dog actualDog = dogDAO.getDogById(dog.getId());
        assertReflectionEquals(dog, actualDog);
    }

    @Test
    public void shouldRemoveDogById() {
        Dog dog = saveNewDog();
        sessionFactory.getCurrentSession().clear();
        int sizeBefore = dogDAO.getAllDogs().size();
        dogDAO.freeDogById(dog.getId());
        assertEquals(sizeBefore - 1, dogDAO.getAllDogs().size());
        assertFalse(dogDAO.getAllDogs().contains(dog));

    }
}