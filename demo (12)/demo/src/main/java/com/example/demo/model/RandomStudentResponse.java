package com.example.demo.model;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
public class RandomStudentResponse {

    protected List<RandomStudent> results;


    public RandomStudentResponse() {
    }

    public RandomStudentResponse(List<RandomStudent> results) {
        this.results = results;
    }
}
