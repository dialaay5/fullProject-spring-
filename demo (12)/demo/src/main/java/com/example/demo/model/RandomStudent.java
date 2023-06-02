package com.example.demo.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RandomStudent {
    protected String gender;
    protected Name name;

    public RandomStudent() {
    }

    public RandomStudent(String gender, Name name) {
        this.gender = gender;
        this.name = name;
    }
}

