package com.epam.dog.dao;

import com.epam.dog.vo.Dog;

import java.time.LocalDate;
import java.util.List;

public interface DogDAO {
    List<Dog> getAllDogs();
    Dog saveDog(String name, int height, int weight, LocalDate dateOfBirth);
    Dog getDogById(int id);
    Dog freeDogById(int id);
    boolean hasDog(int id);
    Dog editDogById(int id, String name, int height, int weight, LocalDate dateOfBirth);
}
