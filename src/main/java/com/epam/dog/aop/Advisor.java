package com.epam.dog.aop;

import org.springframework.aop.AfterReturningAdvice;

import java.lang.reflect.Method;

public class Advisor implements AfterReturningAdvice {
//    @Autowired
//    private DogDAO dogDAO;

    @Override
    public void afterReturning(Object o, Method method, Object[] objects, Object o1) throws Throwable {
        System.out.println("After returning method...");
//        dogDAO.getAllDogs().forEach(System.out::println);

    }



//    public void addAppender(){
//        System.out.println("appender");
//    }
//    public void before(){
//        System.out.println("Before method...");
//    }

//    public void after() {
//        List<Dog> dogs = dogDAO.getAllDogs();
//        System.out.println("After method...");
//        dogs.forEach(System.out::println);
//    }

//    public void afterReturning(){
//        System.out.println("After returning...");
//    }
//    public void afterThrowing(){
//        System.out.println("After throwing...");
//    }
}
