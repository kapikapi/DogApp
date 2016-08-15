package com.epam.dog.dao;

import com.epam.dog.controller.vo.Dog;

import java.util.HashMap;
import java.util.Map;

public class DogHandler implements DogDAO {

    private Map<Integer, Dog> dogs;

    public DogHandler() {
        dogs = new HashMap<>();
        Dog aqua = new Dog();
        aqua.setId(1);
        aqua.setName("Aqua");
        aqua.setHeight(45);
        aqua.setWeight(10);
        dogs.put(aqua.getId(), aqua);
    }

    @Override
    public Map<Integer, Dog> getAllDogs() {
        return dogs;
    }

    @Override
    public int saveDog(Dog dog) {
        dogs.put(dog.getId(), dog);
        return dog.getId();
    }

    @Override
    public Dog getDogById(int id) {
        return dogs.get(id);
    }

    @Override
    public Dog freeDogById(int id) {
        return dogs.remove(id);
    }

    @Override
    public boolean hasDog(int id) {
        return dogs.containsKey(id);
    }
}
