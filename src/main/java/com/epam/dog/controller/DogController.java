package com.epam.dog.controller;

import com.epam.dog.controller.vo.Dog;
import com.epam.dog.controller.vo.DogDto;
import com.epam.dog.dao.DogDAO;
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

//    private final DogDAO dogDAO;
    private final DogDAO dogDAO;

    private DogDto getDogDto(Dog dog) {
        DogDto dogDto = new DogDto();
        dogDto.setName(dog.getName());
        dogDto.setHeight(dog.getHeight());
        dogDto.setWeight(dog.getWeight());
        return dogDto;
    }

    public DogController(DogDAO dogDAO) {
        System.out.println(dogDAO == null);
        this.dogDAO = dogDAO;
    }

    @RequestMapping(value = "/dog", method = RequestMethod.GET)
    public ResponseEntity<List<Dog>> getAllDogs() {
        Map<Integer, Dog> dogsMap = dogDAO.getAllDogs();
        if (dogsMap.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            List<Dog> dogList = new ArrayList<>(dogsMap.values());
            return new ResponseEntity<>(dogList, HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/dog/{id}", method = RequestMethod.GET)
    public ResponseEntity<DogDto> getDog(@PathVariable("id") int id) {

        Dog dog = dogDAO.getDogById(id);

        if (dog == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        DogDto dogDto = getDogDto(dog);
        return new ResponseEntity<>(dogDto, HttpStatus.OK);
    }

    @RequestMapping(value = "/dog", method = RequestMethod.POST)
    public ResponseEntity<DogDto> createDog(@RequestBody DogDto dog, UriComponentsBuilder ucBuilder) {
        int id = dogDAO.saveDog(dog.getName(), dog.getHeight(), dog.getWeight());
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(ucBuilder.path("/dog/{id}").buildAndExpand(id).toUri());
        return new ResponseEntity<>(dog, headers, HttpStatus.CREATED);

    }

    @RequestMapping(value = "/dog/{id}", method = RequestMethod.PUT)
    public ResponseEntity<DogDto> updateDog(@PathVariable("id") int id, @RequestBody DogDto dog) {
        if (!dogDAO.hasDog(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            dogDAO.editDogById(id, dog.getName(), dog.getHeight(), dog.getWeight());
            return new ResponseEntity<>(dog, HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/dog/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<DogDto> deleteDog(@PathVariable("id") int id) {
        if (!dogDAO.hasDog(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            Dog dog = dogDAO.freeDogById(id);
            DogDto dogDto = getDogDto(dog);
            return new ResponseEntity<>(dogDto, HttpStatus.OK);
        }
    }
}
