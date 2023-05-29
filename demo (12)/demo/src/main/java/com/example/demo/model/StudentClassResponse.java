package com.example.demo.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
public class StudentClassResponse {
    protected ClassRoom classRoom;
    protected List<Student> studentList;

    public StudentClassResponse() {
    }

    public StudentClassResponse(ClassRoom classRoom, List<Student> studentList) {
        this.classRoom = classRoom;
        this.studentList = studentList;
    }




}

