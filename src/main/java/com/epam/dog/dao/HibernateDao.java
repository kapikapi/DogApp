package com.epam.dog.dao;

import com.epam.dog.HibernateUtil;
import com.epam.dog.controller.vo.Dog;
import com.epam.dog.controller.vo.DogDto;
import com.epam.dog.controller.vo.DogEntity;
import org.hibernate.Session;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class HibernateDao implements DogDAO {

    private Dog dogEntityToDog(DogEntity dogEntity) {
        return new Dog(dogEntity.getId(), dogEntity.getName(), dogEntity.getHeight(),
                dogEntity.getWeight());
    }

    @Override
    public Map<Integer, Dog> getAllDogs() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        List<DogEntity> result = session.createQuery("from DogEntity").list();
        Map<Integer, Dog> dogsMap = new ConcurrentHashMap<>();
        for (DogEntity dog : result) {
           dogsMap.put(dog.getId(), dogEntityToDog(dog));
        }
        return dogsMap;
    }

    @Override
    public int saveDog(DogDto dogDto) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        DogEntity dogEntity = new DogEntity();
        dogEntity.setName(dogDto.getName());
        dogEntity.setHeight(dogDto.getHeight());
        dogEntity.setWeight(dogDto.getWeight());
        session.save(dogEntity);
        session.getTransaction().commit();
        HibernateUtil.shutdown();

        return dogEntity.getId();
    }

    @Override
    public Dog getDogById(int id) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
//        Map<Integer, Dog> dogMap = getAllDogs();
        DogEntity dog = (DogEntity) session.get(Dog.class, id);
        session.getTransaction().commit();
        HibernateUtil.shutdown();
        return dogEntityToDog(dog);
    }

    @Override
    public Dog freeDogById(int id) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        DogEntity dog = (DogEntity) session.get(Dog.class, id);
        session.delete(dog);
        session.getTransaction().commit();
        HibernateUtil.shutdown();
        return dogEntityToDog(dog);
    }

    @Override
    public boolean hasDog(int id) {
        return getDogById(id) != null;
    }
}
