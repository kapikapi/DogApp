package com.epam.dog.vo;

import com.epam.dog.Balance;

import javax.validation.constraints.Size;

@Balance
public class Dog {

    private int id;
    @Size(min = 1, max = 100)
    private String name;
    private int height;
    private int weight;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getHeight() {
        return height;
    }

    public int getWeight() {
        return weight;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

//    @Override
//    public boolean equals(Object obj) {
//        return obj != null && (obj == this || obj instanceof Dog && ((Dog) obj).getId() == this.getId());
//    }


    @Override
    public String toString() {
        return String.format("[%s:: %s / %s / %s]", id, name, height, weight);
    }
}
