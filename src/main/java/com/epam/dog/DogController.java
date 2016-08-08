package com.epam.dog;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;

@RestController
public class DogController {

    static List<Dog> dogs = new ArrayList<>();

    private static void setDogs() {
        Dog wowDoge = new Dog(dogs.size(), "Wow", 60, 15);
        dogs.add(wowDoge);
        Dog corgiDoge = new Dog(dogs.size(), "Corgi", 40, 10);
        dogs.add(corgiDoge);
    }

    private static Dog getDogById(List<Dog> dogList, int id) {
        setDogs();
        for (Dog dog : dogList) {
            if (dog != null && dog.getId() == id) {
                return dog;
            }
        }
        return null;
    }

    @RequestMapping(value = "/dog/{id}", method = RequestMethod.GET)
    public ResponseEntity<Dog> getDog(@PathVariable("id") int id) {
        Dog dog = DogController.getDogById(dogs, id);
        if (dog == null) {
            System.out.println("Dog with id " + id + " ran away");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(dog, HttpStatus.OK);
    }

    @RequestMapping(value = "/dog", method = RequestMethod.POST)
    public ResponseEntity<Void> createDog(@RequestBody Dog dog, UriComponentsBuilder ucBuilder) {
        System.out.println(dog);
        if (dogs.contains(dog)) {
            System.out.println("Dog with id " + dog.getId() + " already exists");
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        dogs.add(dog);

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(ucBuilder.path("/dog/{id}").buildAndExpand(dog.getId()).toUri());
        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/dog/{id}", method = RequestMethod.PUT)
    public ResponseEntity<Dog> updateDog(@PathVariable("id") int id, @RequestBody Dog dog) {
        Dog updatedDog = getDogById(dogs, id);
        if (updatedDog == null) {
            System.out.println("Dog with id " + id + " ran away");
            return null;
        }
        int index = dogs.indexOf(updatedDog);
        updatedDog.setName(dog.getName());
        updatedDog.setHeight(dog.getHeight());
        updatedDog.setWeight(dog.getWeight());
        dogs.set(index, updatedDog);

        return new ResponseEntity<>(updatedDog, HttpStatus.OK);
    }


    @RequestMapping(value = "/dog/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Dog> deleteUser(@PathVariable("id") int id) {

        Dog dog = getDogById(dogs, id);
        if (dog == null) {
            System.out.println("Unable to delete dor with id " + id + ". Dog with such id is not found");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        dogs.remove(dog);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
