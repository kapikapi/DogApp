package com.epam.dog;

import com.epam.dog.vo.DogDto;

import static io.qala.datagen.RandomShortApi.positiveInteger;
import static io.qala.datagen.RandomShortApi.unicode;

public class DogsHandler {

    public static DogDto setRandomDogDto() {
        DogDto dog = new DogDto();
        dog.setName(unicode(0, 200));
        dog.setHeight(positiveInteger());
        dog.setWeight(positiveInteger());
        return dog;
    }
}
