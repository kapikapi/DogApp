package com.epam.dog.controller;

import com.epam.dog.controller.vo.Dog;
import com.epam.dog.dao.DogDAO;
import com.epam.dog.dao.DogHandler;
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
import java.util.Map;

@Controller
public class DogController {

    private final DogDAO dogDAO = new DogHandler();

    @RequestMapping(value = "/dog", method = RequestMethod.GET)
    public ResponseEntity<List<Dog>> getAllDogs() {
        Map<Integer, Dog> dogsMap = dogDAO.getAllDogs();
        if(dogsMap.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            List<Dog> dogList = new ArrayList<>(dogsMap.values());
            return new ResponseEntity<>(dogList, HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/dog/{id}", method = RequestMethod.GET)
    public ResponseEntity<Dog> getDog(@PathVariable("id") int id) {

        Dog dog = dogDAO.getDogById(id);
        if (dog == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(dog, HttpStatus.OK);
    }

    @RequestMapping(value = "/dog", method = RequestMethod.POST)
    public ResponseEntity<Dog> createDog(@RequestBody Dog dog, UriComponentsBuilder ucBuilder) {
        if (dogDAO.hasDog(dog.getId())) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        } else {
            dogDAO.saveDog(dog);

            HttpHeaders headers = new HttpHeaders();
            headers.setLocation(ucBuilder.path("/dog/{id}").buildAndExpand(dog.getId()).toUri());
            return new ResponseEntity<>(dog, headers, HttpStatus.CREATED);
        }
    }

    @RequestMapping(value = "/dog/{id}", method = RequestMethod.PUT)
    public ResponseEntity<Dog> updateDog(@PathVariable("id") int id, @RequestBody Dog dog) {
        if (!dogDAO.hasDog(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            Dog updatedDog = dogDAO.getDogById(id);
            updatedDog.setName(dog.getName());
            updatedDog.setHeight(dog.getHeight());
            updatedDog.setWeight(dog.getWeight());
            return new ResponseEntity<>(updatedDog, HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/dog/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Dog> deleteDog(@PathVariable("id") int id) {
        if (!dogDAO.hasDog(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(dogDAO.freeDogById(id), HttpStatus.OK);
        }
    }
}
