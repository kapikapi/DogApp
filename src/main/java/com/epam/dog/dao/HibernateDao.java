package com.epam.dog.dao;

import com.epam.dog.HibernateUtil;
import com.epam.dog.vo.Dog;
import org.hibernate.Session;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class HibernateDao implements DogDAO {

    @Override
    public Map<Integer, Dog> getAllDogs() {
        Session session = HibernateUtil.getSession();
        session.beginTransaction();
        List<Dog> result = session.createQuery("from Dog").list();
        session.getTransaction().commit();
        Map<Integer, Dog> dogsMap = new ConcurrentHashMap<>();
        for (Dog dog : result) {
           dogsMap.put(dog.getId(), dog);
        }
        return dogsMap;
    }

    @Override
    public int saveDog(String name, int height,int weight) {
        Session session = HibernateUtil.getSession();
        session.beginTransaction();
        Dog dog = new Dog();
        dog.setName(name);
        dog.setHeight(height);
        dog.setWeight(weight);
        session.save(dog);
        session.getTransaction().commit();
//        HibernateUtil.shutdown();

        return dog.getId();
    }

    @Override
    public Dog getDogById(int id) {
        Session session = HibernateUtil.getSession();
        session.beginTransaction();
//        Map<Integer, Dog> dogMap = getAllDogs();
        Dog dog = (Dog) session.get(Dog.class, id);
        session.getTransaction().commit();
//        HibernateUtil.shutdown();
        return dog;
    }

    @Override
    public Dog freeDogById(int id) {
        Session session = HibernateUtil.getSession();
        session.beginTransaction();
        Dog dog = (Dog) session.get(Dog.class, id);
        session.delete(dog);
        session.getTransaction().commit();
//        HibernateUtil.shutdown();
        return dog;
    }

    @Override
    public boolean hasDog(int id) {
        return getDogById(id) != null;
    }

    @Override
    public Dog editDogById(int id, String name, int height,int weight) {
        Session session = HibernateUtil.getSession();
        session.beginTransaction();
        Dog dog = (Dog) session.get(Dog.class, id);

        dog.setName(name);
        dog.setHeight(height);
        dog.setWeight(weight);
        session.update(dog);
        session.getTransaction().commit();

        return dog;
    }
}
