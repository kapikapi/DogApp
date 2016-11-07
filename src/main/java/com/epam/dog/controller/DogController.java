package com.epam.dog.controller;

import com.epam.dog.dao.DogDAO;
import com.epam.dog.vo.Dog;
import com.epam.dog.vo.DogDto;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.util.List;

@Controller
public class DogController {

    private final DogDAO dogDAO;

    private DogDto getDogDto(Dog dog) {
        DogDto dogDto = new DogDto();
        dogDto.setName(dog.getName());
        dogDto.setHeight(dog.getHeight());
        dogDto.setWeight(dog.getWeight());
        dogDto.setDateOfBirth(dog.getDateOfBirth());
        return dogDto;
    }

    public DogController(DogDAO dogDAO) {
        System.out.println(dogDAO == null);
        this.dogDAO = dogDAO;
    }

    @RequestMapping(value = "/dog", method = RequestMethod.GET)
    public ResponseEntity<List<Dog>> getAllDogs() {
        List<Dog> dogsMap = dogDAO.getAllDogs();
        if (dogsMap.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(dogsMap, HttpStatus.OK);
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
    public ResponseEntity<DogDto> createDog(@RequestBody @Valid DogDto dogDto, UriComponentsBuilder ucBuilder) {
        Dog savedDog = dogDAO.saveDog(dogDto.getName(), dogDto.getHeight(), dogDto.getWeight(), dogDto.getDateOfBirth());
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(ucBuilder.path("/dog/{id}").buildAndExpand(savedDog.getId()).toUri());
        return new ResponseEntity<>(dogDto, headers, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/dog/{id}", method = RequestMethod.PUT)
    public ResponseEntity<DogDto> updateDog(@PathVariable("id") int id, @RequestBody @Valid DogDto dog) {
        if (!dogDAO.hasDog(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            dogDAO.editDogById(id, dog.getName(), dog.getHeight(), dog.getWeight(), dog.getDateOfBirth());
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
