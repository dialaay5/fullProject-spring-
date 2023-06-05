package com.example.demo.repository;

import com.example.demo.model.AllDataAlreadyExistException;
import com.example.demo.model.ClassRoom;
import com.example.demo.model.ClientFaultException;

import java.sql.SQLException;
import java.util.List;

public interface IClassRoomRepository {
    ClassRoom createClassRoom(ClassRoom classRoom) throws ClientFaultException;

    void updateClassRoom(ClassRoom classRoom, Integer id);

    void deleteClassRoom(Integer id);

    List<ClassRoom> getAllClassRooms();

    ClassRoom getClassRoomById(Integer id);

    void synchronizationClassAvg_countOfStudents(Integer id);

}