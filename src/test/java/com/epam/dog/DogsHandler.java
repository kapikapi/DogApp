package com.epam.dog;

import com.epam.dog.vo.DogDto;

import static io.qala.datagen.RandomShortApi.positiveInteger;
import static io.qala.datagen.RandomShortApi.unicode;

public class DogsHandler {

    public static DogDto setCorrectDogDto() {
        DogDto dog = new DogDto();
        dog.setName(unicode(1, 100));
        int height = positiveInteger();
        int weight = positiveInteger();
        weight = weight != height ? weight : weight + 1;
        dog.setHeight(height);
        dog.setWeight(weight);
        return dog;
    }

    public static DogDto setTooLongNameDogDto() {
        DogDto dog = setCorrectDogDto();
        dog.setName(unicode(101, 1000));
        return dog;
    }

    public static DogDto setUnbalancedDogDto() {
        DogDto dog = setCorrectDogDto();
        dog.setWeight(dog.getHeight());
        return dog;
    }
}
