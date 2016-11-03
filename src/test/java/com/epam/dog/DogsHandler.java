package com.epam.dog;

import com.epam.dog.vo.Dog;
import com.epam.dog.vo.DogDto;

import static io.qala.datagen.RandomShortApi.positiveInteger;
import static io.qala.datagen.RandomShortApi.unicode;

public class DogsHandler {

    public static DogDto setRandomDogDto() {
        DogDto dog = new DogDto();
        dog.setName(unicode(0, Integer.MAX_VALUE));
        dog.setHeight(positiveInteger());
        dog.setWeight(positiveInteger());
        return dog;
    }

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

    public static Dog dogDtoToDog(int id, DogDto dogDto) {
        Dog dog = new Dog();
        dog.setId(id);
        dog.setName(dogDto.getName());
        dog.setHeight(dogDto.getHeight());
        dog.setWeight(dogDto.getWeight());
        return dog;
    }
}
