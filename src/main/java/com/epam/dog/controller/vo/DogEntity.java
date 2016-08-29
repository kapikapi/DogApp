package com.epam.dog.controller.vo;

import javax.persistence.*;

@Entity
//@org.hibernate.annotations.Entity(dynamicUpdate = true)
@Table(name = "DOGS", uniqueConstraints = {@UniqueConstraint(columnNames = "ID")})
public class DogEntity {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private int id;

    @Column(name = "DOG_NAME", nullable = false, length = 200)
    private String name;
    @Column(name = "HEIGHT")
    private int height;
    @Column(name = "WEIGHT")
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

//    public void setId(int id) {
//        this.id = id;
//    }

    public void setName(String name) {
        this.name = name;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }
}
