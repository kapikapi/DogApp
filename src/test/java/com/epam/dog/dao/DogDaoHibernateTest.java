package com.epam.dog.dao;

import com.epam.dog.DogsHandler;
import com.epam.dog.vo.Dog;
import com.epam.dog.vo.DogDto;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.springframework.test.context.transaction.AfterTransaction;
import org.springframework.transaction.annotation.Transactional;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import java.util.List;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;
import static org.unitils.reflectionassert.ReflectionAssert.assertReflectionEquals;

@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/mvc-dispatcher-servlet.xml"})
public class DogDaoHibernateTest extends AbstractTransactionalTestNGSpringContextTests{

//    @Qualifier("dogDao")
    @Autowired
    private DogDAO dogDAO;

//    @Qualifier("dogInterfaceProxy")
//    @Autowired
//    private DogDAO dogDAOProxy;

    @Autowired
    private SessionFactory sessionFactory;

    private Dog saveNewDog() {
        DogDto newDog = DogsHandler.setRandomDogDto();
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
    @Rollback
    @Test
    public void shouldReturnAllDogs() {
        Dog dog = saveNewDog();
        sessionFactory.getCurrentSession().clear();
        List<Dog> dogActualList = dogDAO.getAllDogs();
        assertReflectionEquals(dog, getDogFromListById(dogActualList, dog.getId()));
    }

    @Transactional
    @Rollback
    @Test
    public void shouldSaveSpecifiedDog() {
        int sizeBefore = dogDAO.getAllDogs().size();
        Dog dog = saveNewDog();
//        sessionFactory.getCurrentSession().clear();
        List<Dog> updatedList = dogDAO.getAllDogs();
        assertEquals(sizeBefore + 1, updatedList.size());
        assertReflectionEquals(dog, getDogFromListById(updatedList, dog.getId()));
//        dogDAO.getDogById(dog.getId());
        System.out.println(dog.getId());
    }

    @Transactional
    @Rollback
    @Test
    public void shouldUpdateDog() {
        Dog dog = saveNewDog();
        sessionFactory.getCurrentSession().flush();
        sessionFactory.getCurrentSession().clear();
        DogDto newDogDto = DogsHandler.setRandomDogDto();
        Dog updatedDog = new Dog();
        updatedDog.setId(dog.getId());
        updatedDog.setName(newDogDto.getName());
        updatedDog.setHeight(newDogDto.getHeight());
        updatedDog.setWeight(newDogDto.getWeight());
        dogDAO.editDogById(dog.getId(), newDogDto.getName(), newDogDto.getHeight(), newDogDto.getWeight());
        sessionFactory.getCurrentSession().flush();
        sessionFactory.getCurrentSession().clear();
        assertReflectionEquals(dogDAO.getDogById(dog.getId()), updatedDog);
    }

    @Transactional
    @Rollback
    @Test
    public void shouldReturnDogById() {
        Dog dog = saveNewDog();
        sessionFactory.getCurrentSession().clear();
        Dog actualDog = dogDAO.getDogById(dog.getId());
        assertReflectionEquals(dog, actualDog);
    }

    @Transactional
    @Rollback
    @Test
    public void shouldRemoveDogById() {
        Dog dog = saveNewDog();
        sessionFactory.getCurrentSession().clear();
        int sizeBefore = dogDAO.getAllDogs().size();
        dogDAO.freeDogById(dog.getId());
        assertEquals(sizeBefore - 1, dogDAO.getAllDogs().size());
        assertNull(getDogFromListById(dogDAO.getAllDogs(), dog.getId()));

    }

    @AfterMethod
    public void getActualDogsListBeforeRollback() {
        System.out.println("== After test method");
        System.out.println("== Dog still remains in the database:");
        System.out.println("Hibernate: " + dogDAO.getAllDogs());

    }

    @AfterTransaction
    public void getActualDogsListAfterRollback() {
        System.out.println("== After transaction end");
        System.out.println("== Dog removed from database:");
        System.out.println("Hibernate: " + dogDAO.getAllDogs());
    }
}