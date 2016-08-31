package com.epam.dog;

import com.epam.dog.controller.vo.DogDto;

import static io.qala.datagen.RandomShortApi.english;
import static io.qala.datagen.RandomShortApi.integer;

public class DogsHandler {

    public static DogDto setRandomDogDto() {
        DogDto dog = new DogDto();
        dog.setName(english(10));
        dog.setHeight(integer(20, 100));
        dog.setWeight(integer(3, 70));
        return dog;
    }

}
