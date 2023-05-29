package com.example.demo.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class ClassRoom {
    protected Integer id;
    protected Integer numberOfStudents;
    protected Float classAvg;
    protected ClassRoomType classRoomType;

    public ClassRoom() {
    }

    public ClassRoom(Integer id, Integer numberOfStudents, Float classAvg, ClassRoomType classRoomType) {
        this.id = id;
        this.numberOfStudents = numberOfStudents;
        this.classAvg = classAvg;
        this.classRoomType = classRoomType;
    }

    @Override
    public String toString() {
        return "ClassRoom{" +
                "id=" + id +
                ", numberOfStudents=" + numberOfStudents +
                ", classAvg=" + classAvg +
                ", classRoomType=" + classRoomType.name() +
                '}';
    }
}
