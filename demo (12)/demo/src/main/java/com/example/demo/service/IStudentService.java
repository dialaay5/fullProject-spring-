package com.example.demo.service;
import com.example.demo.model.*;

import java.util.List;

public interface IStudentService {
    Student createStudent(Student student) throws ClientFaultException;

    void updateStudent(Student student, Integer id) throws ClientFaultException;

    void deleteStudent(Integer id);

    List<Student> getAllStudents();

    Student getStudentById(Integer id);
    List<Student> getStudentsByClassRoomExternalType();
    StudentClassResponse DtoStudentsClass(Integer id);
}
