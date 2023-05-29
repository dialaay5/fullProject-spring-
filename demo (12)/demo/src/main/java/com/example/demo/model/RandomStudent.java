package com.example.demo.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RandomStudent {
    protected StudentGender gender;
    protected Name name;

    public RandomStudent() {
    }

    public RandomStudent(StudentGender gender, Name name) {
        this.gender = gender;
        this.name = name;
    }
}

