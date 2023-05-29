package com.example.demo.service;
import com.example.demo.model.Student;
import com.example.demo.model.StudentClassResponse;

import java.util.List;

public interface IStudentService {
    String createStudent(Student student);

    String updateStudent(Student student, Integer id);

    String deleteStudent(Integer id);

    List<Student> getAllStudents();

    Student getStudentById(Integer id);
    List<Student> getStudentsByClassRoomExternalType();
    StudentClassResponse DtoStudentsClass(Integer id);
}
