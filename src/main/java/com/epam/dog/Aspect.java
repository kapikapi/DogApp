package com.epam.dog;

import com.epam.dog.dao.DogDAO;
import com.epam.dog.vo.Dog;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class Aspect {

    @Autowired
    private DogDAO dogDAO;

    public void addAppender(){
        System.out.println("appender");
    }
    public void before(){
        System.out.println("Before method...");
    }
    public void after(){
        List<Dog> dogs = dogDAO.getAllDogs();
        System.out.println("After method...");
        dogs.forEach(System.out::println);
    }
    public void afterReturning(){
        System.out.println("After returning...");
    }
    public void afterThrowing(){
        System.out.println("After throwing...");
    }
}
