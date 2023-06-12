package com.example.demo.service;

import com.example.demo.model.*;
import com.example.demo.repository.CacheRepositoryImpl;
import com.example.demo.repository.ClassRoomRepository;
import com.example.demo.repository.StudentRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
    private CacheRepositoryImpl cacheRepository;
    @Autowired
    private ObjectMapper objectMapper;

    @Value("${cache_on}")
    private Boolean cache_on;

    @Autowired
    RandomStudentService apiClient;

    @Value("${maxstudents}")
    private Integer maxstudents;
    @Override
    public Student createStudent(Student student) throws ClientFaultException, JsonProcessingException {
        if (classRoomRepository.getClassRoomById(student.getClass_id()).getClassRoomType().equals(ClassRoomType.EXTERNAL) &&
                classRoomRepository.getClassRoomById(student.getClass_id()).getNumberOfStudents() >= maxstudents){
            throw new ExceedExternalException("Cannot create more students in external classRoom");
        }
        Student result = studentRepository.createStudent(student);
        classRoomRepository.synchronizationClassAvg_countOfStudents(student.getClass_id());

        //אם הוספנו תלמיד חדש, צריך לעדכן את הפרטים של הכיתה שתהווסף אליה התלמיד גם בדאטה וגם ברידס אם נמצאת
        ClassRoom classRoom = classRoomRepository.getClassRoomById(student.getClass_id()) ;
        if(cache_on && cacheRepository.isKeyExist("CLASS" + (student.getClass_id()).toString())){
            cacheRepository.updateCacheEntity("CLASS" + (student.getClass_id()).toString(), objectMapper.writeValueAsString(classRoom));
        }
        return result;
    }

    @Override
    public void updateStudent(Student student, Integer id) throws ClientFaultException, JsonProcessingException {
        Integer class_id_before_update = studentRepository.getStudentById(id).getClass_id();
        if(class_id_before_update.intValue() !=  student.getClass_id().intValue()){
            throw new CannotChangeClassIdException("Cannot change class_id");
        }
        studentRepository.updateStudent(student,id);
        classRoomRepository.synchronizationClassAvg_countOfStudents(class_id_before_update);

        ClassRoom result = classRoomRepository.getClassRoomById(class_id_before_update) ;

        if (cache_on && cacheRepository.isKeyExist("STUDENT" + id.toString())) {
            cacheRepository.updateCacheEntity("STUDENT" + id.toString(), objectMapper.writeValueAsString(student));
            if(cache_on && cacheRepository.isKeyExist("CLASS" + class_id_before_update.toString())){
                cacheRepository.updateCacheEntity("CLASS" + class_id_before_update.toString(), objectMapper.writeValueAsString(result));
            }
        }
    }

    @Override
    public void deleteStudent(Integer id) throws JsonProcessingException {
        Integer student_classRoom_id = studentRepository.getStudentById(id).getClass_id();
        studentRepository.deleteStudent(id);
        classRoomRepository.synchronizationClassAvg_countOfStudents(student_classRoom_id);

        ClassRoom result = classRoomRepository.getClassRoomById(student_classRoom_id) ;

        if (cache_on && cacheRepository.isKeyExist("STUDENT" + id.toString())) {
            cacheRepository.removeKey("STUDENT" + id.toString());
            if(cache_on && cacheRepository.isKeyExist("CLASS" + student_classRoom_id.toString())){
                cacheRepository.updateCacheEntity("CLASS" + student_classRoom_id.toString(), objectMapper.writeValueAsString(result));
            }
        }
    }



    @Override
    public List<Student> getAllStudents() {
        return studentRepository.getAllStudents();
    }

    @Override
    public Student getStudentById(Integer id) {
        try {
            if (cache_on && cacheRepository.isKeyExist("STUDENT" + id.toString())) {
                System.out.println("is Key Exist? " + cacheRepository.isKeyExist("STUDENT" + id.toString()));
                String student = cacheRepository.getCacheEntity("STUDENT" + id.toString());
                System.out.println("reading from cache " + student);
                return objectMapper.readValue(student, Student.class);
            }

            Student result = studentRepository.getStudentById(id);

            if (cache_on) {
                cacheRepository.createCacheEntity("STUDENT" + id.toString(), objectMapper.writeValueAsString(result));
            }
            return result;

        } catch (JsonProcessingException e) {
            System.out.println(e);
            throw new IllegalStateException("cannot write json of student");
        }
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

