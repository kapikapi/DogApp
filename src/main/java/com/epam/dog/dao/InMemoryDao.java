package com.epam.dog.dao;

import com.epam.dog.vo.Dog;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryDao implements DogDAO {

    private Map<Integer, Dog> dogs;

    public InMemoryDao() {
        dogs = new ConcurrentHashMap<>();
//        Dog aqua = new Dog(dogs.size() + 1, "Aqua", 45, 10);
//        dogs.put(aqua.getId(), aqua);
    }

    @Override
    public Map<Integer, Dog> getAllDogs() {
        return dogs;
    }

    @Override
    public int saveDog(String name, int height, int weight) {
        Dog dog = new Dog();
        dog.setId(dogs.size() + 1);
        dog.setName(name);
        dog.setHeight(height);
        dog.setWeight(weight);
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
    public Dog editDogById(int id, String name, int height,int weight) {
        Dog dog = new Dog();
        dog.setId(id);
        dog.setName(name);
        dog.setHeight(height);
        dog.setWeight(weight);
        dogs.put(id, dog);
        return dog;
    }
}
