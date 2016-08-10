package com.epam.dog.controller;

import com.epam.dog.controller.vo.Dog;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.Map;

@Controller
public class DogController {

    private static final Map<Integer, Dog> dogsById = new HashMap<>();

    private static Dog getDogById(int id) {
        return dogsById.get(id);
    }

    private static int saveDog(Dog dog) {
        dogsById.put(dog.getId(), dog);
        return dog.getId();
    }

    private static Dog freeDogById(int id) {
        return dogsById.remove(id);
    }

    private static boolean hasDog(int id) {
        return dogsById.containsKey(id);
    }

    @RequestMapping(value = "/dog", method = RequestMethod.GET)
    public ResponseEntity<Map<Integer, Dog>> getAllDogs() {
        if(dogsById.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(dogsById, HttpStatus.OK);
    }

    @RequestMapping(value = "/dog/{id}", method = RequestMethod.GET)
    public ResponseEntity<Dog> getDog(@PathVariable("id") int id) {

        Dog dog = getDogById(id);
        if (dog == null) {
            System.out.println("Dog with id " + id + " ran away");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(dog, HttpStatus.OK);
    }

    @RequestMapping(value = "/dog", method = RequestMethod.POST)
    public ResponseEntity<Dog> createDog(@RequestBody Dog dog, UriComponentsBuilder ucBuilder) {
        if (hasDog(dog.getId())) {
            System.out.println("Dog with id " + dog.getId() + " already exists");
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        } else {
            saveDog(dog);

            HttpHeaders headers = new HttpHeaders();
            headers.setLocation(ucBuilder.path("/dog/{id}").buildAndExpand(dog.getId()).toUri());
            return new ResponseEntity<>(dog, headers, HttpStatus.CREATED);
        }
    }

    @RequestMapping(value = "/dog/{id}", method = RequestMethod.PUT)
    public ResponseEntity<Dog> updateDog(@PathVariable("id") int id, @RequestBody Dog dog) {
        if (!hasDog(id)) {
            System.out.println("Dog with id " + id + " ran away");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            Dog updatedDog = getDogById(id);
            updatedDog.setName(dog.getName());
            updatedDog.setHeight(dog.getHeight());
            updatedDog.setWeight(dog.getWeight());
            return new ResponseEntity<>(updatedDog, HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/dog/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Dog> deleteDog(@PathVariable("id") int id) {
        if (!hasDog(id)) {
            System.out.println("Unable to delete dor with id " + id + ". Dog with such id is not found");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(freeDogById(id), HttpStatus.OK);
        }
    }
}
