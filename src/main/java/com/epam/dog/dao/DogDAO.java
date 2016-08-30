package com.epam.dog.dao;

import com.epam.dog.controller.vo.Dog;
import com.epam.dog.controller.vo.DogDto;

import java.util.Map;

public interface DogDAO {
    Map<Integer, Dog> getAllDogs();
    int saveDog(DogDto dogDto);
    Dog getDogById(int id);
    Dog freeDogById(int id);
    boolean hasDog(int id);
    Dog editDogById(int id, DogDto dogDto);
}
