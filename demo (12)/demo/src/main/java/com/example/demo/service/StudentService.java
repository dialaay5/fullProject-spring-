package com.example.demo.service;

import com.example.demo.model.*;
import com.example.demo.repository.ClassRoomRepository;
import com.example.demo.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class StudentService implements IStudentService{
    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private ClassRoomRepository classRoomRepository;

    @Autowired
    RandomStudentService apiClient;

    @Value("${maxstudents}")
    private Integer maxstudents;
    @Override
    public String createStudent(Student student) {
        if (classRoomRepository.getClassRoomById(student.getClass_id()).getClassRoomType().equals(ClassRoomType.EXTERNAL) &&
                classRoomRepository.getClassRoomById(student.getClass_id()).getNumberOfStudents() >= maxstudents){
            return  "{\"Warning\": \"Cannot create more students in external classRoom\" }";
        }
        String result = studentRepository.createStudent(student);
        classRoomRepository.synchronizationClassAvg_countOfStudents(student.getClass_id());
        return result;
    }

    @Override
    public String updateStudent(Student student, Integer id) {
        Integer class_id_before_update = studentRepository.getStudentById(id).getClass_id();
        String result = studentRepository.updateStudent(student,id);
        classRoomRepository.synchronizationClassAvg_countOfStudents(class_id_before_update);
        return result;
    }

    @Override
    public String deleteStudent(Integer id) {
        Integer student_classRoom_id = studentRepository.getStudentById(id).getClass_id();
        String result = studentRepository.deleteStudent(id);
        classRoomRepository.synchronizationClassAvg_countOfStudents(student_classRoom_id);
        return result;

    }

    @Override
    public List<Student> getAllStudents() {
        return studentRepository.getAllStudents();
    }

    @Override
    public Student getStudentById(Integer id) {
        return studentRepository.getStudentById(id);
    }

    @Override
    public List<Student> getStudentsByClassRoomExternalType() {
        return studentRepository.getStudentsByClassRoomType();
    }

    // DTO
    @Override
    public StudentClassResponse DtoStudentsClass(Integer id){
        ClassRoom selectedClass = classRoomRepository.getClassRoomById(id);
        Student classStudentToCreat = new Student();
        List<Student> students = studentRepository.getStudentsByClassRoomId(id);
        return classStudentToCreat.toStudentClassResponse(selectedClass, students);
    }
}

