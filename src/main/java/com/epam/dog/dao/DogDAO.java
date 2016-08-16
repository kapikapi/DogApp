package com.epam.dog.dao;

import com.epam.dog.controller.vo.Dog;

import java.util.Map;

public interface DogDAO {
    Map<Integer, Dog> getAllDogs();
    int saveDog(Dog dog);
    Dog getDogById(int id);
    Dog freeDogById(int id);
    boolean hasDog(int id);
}
