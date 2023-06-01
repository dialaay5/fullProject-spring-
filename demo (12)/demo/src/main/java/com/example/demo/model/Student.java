package com.example.demo.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter

public class Student {
    protected Integer id;
    protected String lastName;
    protected String firstName;
    protected Float avgGrade;
    protected StudentGender gender;
    protected Integer class_id;

    public Student() {
    }

    public Student(Integer id, String lastName, String firstName, Float avgGrade, StudentGender gender, Integer class_id) {
        this.id = id;
        this.lastName = lastName;
        this.firstName = firstName;
        this.avgGrade = avgGrade;
        this.gender = gender;
        this.class_id = class_id;
    }

    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", lastName='" + lastName + '\'' +
                ", firstName='" + firstName + '\'' +
                ", avgGrade=" + avgGrade +
                ", gender=" + gender.name() +
                ", class_id=" + class_id +
                '}';
    }
}
