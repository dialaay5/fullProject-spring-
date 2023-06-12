package com.example.demo.service;

import com.example.demo.model.ClassRoom;
import com.example.demo.model.ClientFaultException;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.List;

public interface IClassRoomService {
    ClassRoom createClassRoom(ClassRoom classRoom) throws ClientFaultException;

    void updateClassRoom(ClassRoom classRoom, Integer id) throws ClientFaultException, JsonProcessingException;

    void deleteClassRoom(Integer id) throws ClientFaultException;

    List<ClassRoom> getAllClassRooms();

    ClassRoom getClassRoomById(Integer id);
}

