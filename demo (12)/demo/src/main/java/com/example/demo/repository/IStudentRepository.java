package com.example.demo.repository;

import com.example.demo.model.Student;

import java.util.List;

public interface IStudentRepository {
    String createStudent(Student student);

    String updateStudent(Student student, Integer id);

    String deleteStudent(Integer id);

    List<Student> getAllStudents();

    Student getStudentById(Integer id);
    List<Student> getStudentsByClassRoomType();
    List<Student> getStudentsByClassRoomId(Integer class_id);

}
