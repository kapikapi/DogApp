package com.epam.dog.dao;

import com.epam.dog.controller.vo.Dog;
import com.epam.dog.controller.vo.DogDto;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryDao implements DogDAO {

    private Map<Integer, Dog> dogs;

    public InMemoryDao() {
        dogs = new ConcurrentHashMap<>();
        Dog aqua = new Dog(dogs.size() + 1, "Aqua", 45, 10);
        dogs.put(aqua.getId(), aqua);
    }

    @Override
    public Map<Integer, Dog> getAllDogs() {
        return dogs;
    }

    @Override
    public int saveDog(DogDto dogDto) {
        Dog dog = new Dog(dogs.size() + 1, dogDto.getName(), dogDto.getHeight(), dogDto.getWeight());
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

    @Override
    public Dog editDogById(int id, DogDto dogDto) {
        Dog dog = new Dog(id, dogDto.getName(), dogDto.getHeight(), dogDto.getWeight());
        dogs.put(id, dog);
        return dog;
    }
}
