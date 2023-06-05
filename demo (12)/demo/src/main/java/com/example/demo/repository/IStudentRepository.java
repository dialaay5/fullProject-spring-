package com.example.demo.repository;

import com.example.demo.model.AllDataAlreadyExistException;
import com.example.demo.model.ClientFaultException;
import com.example.demo.model.Student;

import java.util.List;

public interface IStudentRepository {
    Student createStudent(Student student) throws ClientFaultException;

    void updateStudent(Student student, Integer id);

    void deleteStudent(Integer id);

    List<Student> getAllStudents();

    Student getStudentById(Integer id);
    List<Student> getStudentsByClassRoomType();
    List<Student> getStudentsByClassRoomId(Integer class_id);

}
