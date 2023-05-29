package com.example.demo.repository;

import com.example.demo.model.ClassRoom;

import java.util.List;

public interface IClassRoomRepository {
    String createClassRoom(ClassRoom classRoom);

    String updateClassRoom(ClassRoom classRoom, Integer id);

    String deleteClassRoom(Integer id);

    List<ClassRoom> getAllClassRooms();

    ClassRoom getClassRoomById(Integer id);

    void synchronizationClassAvg_countOfStudents(Integer id);

}