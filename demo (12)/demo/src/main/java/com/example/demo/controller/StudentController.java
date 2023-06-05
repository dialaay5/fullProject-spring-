package com.example.demo.controller;

import com.example.demo.model.*;
import com.example.demo.service.ClassRoomService;
import com.example.demo.service.RandomStudentService;
import com.example.demo.service.StudentService;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Random;

@RestController
@RequestMapping("/api/student")
@CrossOrigin
public class StudentController {
    @Autowired
    StudentService studentService;
    @Autowired
    ClassRoomService classRoomService;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    RandomStudentService apiClient;

    @GetMapping
    public ResponseEntity get(){
    try {
        List<Student> list = studentService.getAllStudents();
        return new ResponseEntity<List<Student>>(list, HttpStatus.OK);
    } catch (Exception e) {
        return new ResponseEntity<String>("{ \"Error\": \"" + e.toString() + "\" }",
                HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

    @GetMapping(value ="/{id}")
    public ResponseEntity getById(@PathVariable Integer id){
            Student result = studentService.getStudentById(id);
            if (result != null) {
                return new ResponseEntity<Student>(result, HttpStatus.OK);
            }
            return new ResponseEntity<String>("{ \"Warning\": \"not found student with Id " + id + "\" }",
                    HttpStatus.NOT_FOUND);
    }

    @PostMapping()
    public ResponseEntity post(@RequestBody Student student){
        try{
            Student postStudent = studentService.createStudent(student);
            return new ResponseEntity<Student>(postStudent, HttpStatus.CREATED);
            }
        catch (ClientFaultException e){
            return new ResponseEntity<String>("{ \"Client Error\": \"" +e.toString() + "\" }",
                    HttpStatus.BAD_REQUEST);
        }
        catch (Exception e) {
            return new ResponseEntity<String>("{ \"Error\": \"" + e.toString() + "\" }",
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity  put(@PathVariable Integer id, @RequestBody Student student) {
        try{
            studentService.updateStudent(student, id);
            return new ResponseEntity<Student>(student, HttpStatus.OK);
        }
        catch (ClientFaultException e){
            return new ResponseEntity<String>("{ \"Client Error\": \"" +e.toString() + "\" }",
                    HttpStatus.BAD_REQUEST);
        }
        catch (Exception e) {
            return new ResponseEntity<String>("{ \"Error\": \"" + e.toString() + "\" }",
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<String> delete(@PathVariable Integer id){
        try{
            Student studentById = studentService.getStudentById(id);
            if (studentById != null) {
                studentService.deleteStudent(id);
                return new ResponseEntity<String>("{ \"Result\": \"student deleted\" }", HttpStatus.OK);
            }
            return new ResponseEntity<String>("{ \"Warning\": \"not found student with this Id = " + id + "\" }",
                    HttpStatus.NOT_FOUND);
        }
        catch (Exception e) {
            return new ResponseEntity<String>("{ \"Error\": \"" + e.toString() + "\" }",
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping(value = "/students")
    public ResponseEntity getStudentsByExternalType(){
        //to get list of students in external classes (challenge)
        try {
            List<Student> result = studentService.getStudentsByClassRoomExternalType();
            return new ResponseEntity<List<Student>>(result, HttpStatus.OK);
        }
        catch (Exception e) {
            return new ResponseEntity<String>("{ \"Error\": \"" + e.toString() + "\" }",
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    // DTO to get list of students in specific class
    @GetMapping(value = "/dto/{id}")
    public ResponseEntity dto(@PathVariable Integer id){
        try {
            StudentClassResponse result = studentService.DtoStudentsClass(id);
            if (result.getClassRoom() != null) {
                return new ResponseEntity<StudentClassResponse>(result, HttpStatus.OK);
            }
            return new ResponseEntity<String>("{ \"Warning\": \"not found this class" + "\" }",
                    HttpStatus.NOT_FOUND);
        }
        catch (Exception e) {
            return new ResponseEntity<String>("{ \"Error\": \"" + e.toString() + "\" }",
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(value = "randomStudent")
    public ResponseEntity postRandomStudent() {
        //get random class
        Integer listSize = classRoomService.getAllClassRooms().size();
        Random rand = new Random();
        ClassRoom randomElement = classRoomService.getAllClassRooms().get(rand.nextInt(listSize));
        Integer class_id = randomElement.getId();

        //get random avgGdare
        Random random = new Random();
        Float avgGrade =  random.nextFloat(100 - 40) + 40;

        //get response from random user api
        RandomStudentResponse response = apiClient.getRandomStudent();
        objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);

        //to convert a String value of gender to an enum
        StudentGender studentGender = StudentGender.valueOf((response.getResults().get(0).getGender()).toUpperCase());


        //create new student and insert to student table
        Student student = new Student(0,response.getResults().get(0).getName().getLastName()
                ,response.getResults().get(0).getName().getFirstName()
                ,avgGrade
                ,studentGender
                ,class_id);
        try{
            Student result = studentService.createStudent(student);
            return new ResponseEntity<Student>(result, HttpStatus.CREATED);
            }
        catch (ClientFaultException e){
            return new ResponseEntity<String>("{ \"Client Error\": \"" +e.toString() + "\" }",
                    HttpStatus.BAD_REQUEST);
        }
        catch (Exception e) {
            return new ResponseEntity<String>("{ \"Error\": \"" + e.toString() + "\" }",
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

