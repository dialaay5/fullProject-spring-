package com.example.demo.service;

import com.example.demo.model.*;
import com.example.demo.repository.ClassRoomRepository;
import com.example.demo.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

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
    public Student createStudent(Student student) throws ClientFaultException {
        if (classRoomRepository.getClassRoomById(student.getClass_id()).getClassRoomType().equals(ClassRoomType.EXTERNAL) &&
                classRoomRepository.getClassRoomById(student.getClass_id()).getNumberOfStudents() >= maxstudents){
            throw new ExceedExternalException("Cannot create more students in external classRoom");
        }
        Student result = studentRepository.createStudent(student);
        classRoomRepository.synchronizationClassAvg_countOfStudents(student.getClass_id());
        return result;
    }

    @Override
    public void updateStudent(Student student, Integer id) throws ClientFaultException {
        Integer class_id_before_update = studentRepository.getStudentById(id).getClass_id();
        if(class_id_before_update.intValue() !=  student.getClass_id().intValue()){
            throw new CannotChangeClassIdException("Cannot change class_id");
        }
        studentRepository.updateStudent(student,id);
        classRoomRepository.synchronizationClassAvg_countOfStudents(class_id_before_update);
    }

    @Override
    public void deleteStudent(Integer id) {
        Integer student_classRoom_id = studentRepository.getStudentById(id).getClass_id();
        studentRepository.deleteStudent(id);
        classRoomRepository.synchronizationClassAvg_countOfStudents(student_classRoom_id);
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

    // DTO RESPONSE
    @Override
    public StudentClassResponse DtoStudentsClass(Integer id){
        ClassRoom selectedClass = classRoomRepository.getClassRoomById(id);
        List<Student> students = studentRepository.getStudentsByClassRoomId(id);
        StudentClassResponse dtoResponse = new StudentClassResponse();
        return dtoResponse.response(selectedClass, students);
    }
}

