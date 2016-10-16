package com.epam.dog;

import com.epam.dog.aop.DogDaoLogger;
import com.epam.dog.dao.DogDAO;
import com.epam.dog.vo.Dog;
import com.epam.dog.vo.DogDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.springframework.transaction.annotation.Transactional;
import org.testng.annotations.Test;

import java.lang.reflect.Proxy;

@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/mvc-dispatcher-servlet.xml"})
public class DogDaoLoggerTest extends AbstractTransactionalTestNGSpringContextTests {

    @Autowired
    private DogDAO dogDAO;

    @Transactional
    @Rollback
    @Test
    public void shouldCheckProxy() {
        DogDto dog = DogsHandler.setRandomDogDto();
        DogDAO proxy = (DogDAO) Proxy.newProxyInstance(DogDAO.class.getClassLoader(),
                dogDAO.getClass().getInterfaces(),
                new DogDaoLogger(dogDAO));
        Dog result = proxy.saveDog(dog.getName(), dog.getHeight(), dog.getWeight());
        System.out.println(result.getId() + ": " + result.getName());
    }
}
