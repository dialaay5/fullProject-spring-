package com.example.demo.service;

import com.example.demo.model.ClassRoom;

import java.util.List;

public interface IClassRoomService {
    String createClassRoom(ClassRoom classRoom);

    String updateClassRoom(ClassRoom classRoom, Integer id) throws Exception;

    String deleteClassRoom(Integer id) throws Exception;

    List<ClassRoom> getAllClassRooms();

    ClassRoom getClassRoomById(Integer id);
}

