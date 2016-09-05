package com.epam.dog.controller.vo;

public class Dog {

    private int id;
    private String name;
    private int height;
    private int weight;

//    public Dog(int id, String name, int height, int weight) {
//        this.id = id;
//        this.name = name;
//        this.height = height;
//        this.weight = weight;
//    }

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
}
