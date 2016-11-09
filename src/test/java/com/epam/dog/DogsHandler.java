package com.epam.dog;

import com.epam.dog.vo.DogDto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static io.qala.datagen.RandomDate.*;
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
        dog.setDateOfBirth(between(yearsAgo(30), now()).localDate());
        return dog;
    }

    public static DogDto setTooLongNameDogDto() {
        DogDto dog = setCorrectDogDto();
        dog.setName(unicode(101, 65536));
        return dog;
    }

    public static DogDto setUnbalancedDogDto() {
        DogDto dog = setCorrectDogDto();
        dog.setWeight(dog.getHeight());
        return dog;
    }

    public static DogDto setFutureBornDogDto() {
        DogDto dog = setCorrectDogDto();
        dog.setDateOfBirth(afterNow().localDate());
        return dog;
    }

    public static List<Integer> localDateToListFormat(LocalDate localDate) {
        List<Integer> list = new ArrayList<>(3);
        list.add(localDate.getYear());
        list.add(localDate.getMonthValue());
        list.add(localDate.getDayOfMonth());
        return list;
    }
}
