package com.example.demo.service;
import com.example.demo.model.*;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.List;

public interface IStudentService {
    Student createStudent(Student student) throws ClientFaultException, JsonProcessingException;

    void updateStudent(Student student, Integer id) throws ClientFaultException, JsonProcessingException;

    void deleteStudent(Integer id) throws JsonProcessingException;

    List<Student> getAllStudents();

    Student getStudentById(Integer id);
    List<Student> getStudentsByClassRoomExternalType();
    StudentClassResponse DtoStudentsClass(Integer id);
}
