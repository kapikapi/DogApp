package com.epam.dog;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;

@Controller
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
        for(Dog dog : dogList) {
            if(dog != null && dog.getId() == id) {
                return dog;
            }
        }
        return null;
    }

    @RequestMapping(value = "/dog/{id}", method = RequestMethod.GET)
    public ResponseEntity<String> getDog(@PathVariable("id") int id) {

        Dog dog = DogController.getDogById(dogs, id);
        if (dog == null) {
            System.out.println("Dog with id " + id + " ran away");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        ObjectMapper mapper = new ObjectMapper();
        try {
            String jsonDog = mapper.writeValueAsString(dog);
            return new ResponseEntity<>(jsonDog, HttpStatus.OK);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.I_AM_A_TEAPOT);
        }

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
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        int index = dogs.indexOf(updatedDog);
        updatedDog.setName(dog.getName());
        updatedDog.setHeight(dog.getHeight());
        updatedDog.setWeight(dog.getWeight());
        dogs.set(index, updatedDog);
        return new ResponseEntity<>(updatedDog, HttpStatus.OK);
    }
}
