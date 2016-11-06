package com.epam.dog.vo;

import com.epam.dog.Balance;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Balance
public class DogDto {

    @Size(min = 1, max = 100, message = "Size must be between 1 and 100")
    private String name;
    @NotNull
    private int height;
    @NotNull
    private int weight;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }
}
