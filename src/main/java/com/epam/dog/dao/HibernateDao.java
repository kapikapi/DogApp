package com.epam.dog.dao;

import com.epam.dog.vo.Dog;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public class HibernateDao implements DogDAO {

    private SessionFactory sessionFactory;

    public HibernateDao(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    @Transactional
    public List<Dog> getAllDogs() {
        Session session = this.sessionFactory.getCurrentSession();
        return session.createQuery("from Dog").list();
    }

    @Override
    @Transactional
    public Dog saveDog(String name, int height,int weight) {
        Session session = this.sessionFactory.getCurrentSession();
        Dog dog = new Dog();
        dog.setName(name);
        dog.setHeight(height);
        dog.setWeight(weight);
        session.save(dog);
        return dog;
    }

    @Override
    @Transactional
    public Dog getDogById(int id) {
        Session session = this.sessionFactory.getCurrentSession();
        Dog dog = (Dog) session.get(Dog.class, id);
        return dog;
    }

    @Override
    @Transactional
    public Dog freeDogById(int id) {
        Session session = this.sessionFactory.getCurrentSession();
        Dog dog = (Dog) session.get(Dog.class, id);
        session.delete(dog);
        return dog;
    }

    @Override
    @Transactional
    public boolean hasDog(int id) {
        return getDogById(id) != null;
    }

    @Override
    @Transactional
    public Dog editDogById(int id, String name, int height,int weight) {
        Session session = this.sessionFactory.getCurrentSession();
        Dog dog = (Dog) session.get(Dog.class, id);
        dog.setName(name);
        dog.setHeight(height);
        dog.setWeight(weight);
        session.update(dog);
        return dog;
    }
}
