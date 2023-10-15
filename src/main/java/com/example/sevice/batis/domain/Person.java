package com.example.sevice.batis.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;
import java.util.Map;


@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Person {
    private String name;
    private int age;
    private Address address;
    private List<ClothsSize> clothsSizes;
    private boolean hasPets;
    private List<String> hobbies;
    private Map<String, Integer> scores;

    public Person() {
    }

    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }
}