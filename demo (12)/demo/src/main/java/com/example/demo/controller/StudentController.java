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
    public List<Student> get()
    {
        return studentService.getAllStudents();
    }

    @GetMapping(value ="/{id}")
    public ResponseEntity getById(@PathVariable Integer id){
        try {
            Student result = studentService.getStudentById(id);
            if (result != null) {
                return new ResponseEntity<Student>(result, HttpStatus.OK);
            }
            return new ResponseEntity<String>("{ \"Warning\": \"not found student with Id " + id + "\" }",
                    HttpStatus.NOT_FOUND);
        }
        catch (Exception e) {
            return new ResponseEntity<String>("{ \"Error\": \"" + e.toString() + "\" }",
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping()
    public ResponseEntity<String> post(@RequestBody Student student){
        try{
            String result = studentService.createStudent(student);
            if (result == null) {
                return new ResponseEntity<String>("{ \"result\": \"student created\" }", HttpStatus.CREATED);
            }
            return new ResponseEntity<String>(result, HttpStatus.BAD_REQUEST);
        }
        catch (Exception e) {
            return new ResponseEntity<String>("{ \"Error\": \"" + e.toString() + "\" }",
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<String>  put(@PathVariable Integer id, @RequestBody Student student) {
        try{
            String result = studentService.updateStudent(student, id);
            if (result == null) {
                return new ResponseEntity<String>("{ \"result\": \"student updated\" }", HttpStatus.OK);
            }
            return new ResponseEntity<String>(result, HttpStatus.BAD_REQUEST);
        }
        catch (Exception e) {
            return new ResponseEntity<String>("{ \"Error\": \"" + e.toString() + "\" }",
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<String> delete(@PathVariable Integer id){
        try{
            String result = studentService.deleteStudent(id);
            if (result == null) {
                return new ResponseEntity<String>("{ \"result\": \"student deleted\" }", HttpStatus.OK);
            }
            return new ResponseEntity<String>(result, HttpStatus.BAD_REQUEST);
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
            if (result.size() != 0) {
                return new ResponseEntity<List<Student>>(result, HttpStatus.OK);
            }
            return new ResponseEntity<String>("{ \"Warning\": \"not found students in external classes " + "\" }",
                    HttpStatus.NOT_FOUND);
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
    public ResponseEntity<String> postRandomStudent() {
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

        //create new student and insert to student table
        Student student = new Student(0,response.getResults().get(0).getName().getLastName()
                ,response.getResults().get(0).getName().getFirstName()
                ,avgGrade
                ,response.getResults().get(0).getGender()
                ,class_id);
        try{
            String result = studentService.createStudent(student);
            if (result == null) {
                return new ResponseEntity<String>("{ \"result\": \"random student created\" }", HttpStatus.CREATED);
            }
            return new ResponseEntity<String>(result, HttpStatus.BAD_REQUEST);
        }
        catch (Exception e) {
            return new ResponseEntity<String>("{ \"Error\": \"" + e.toString() + "\" }",
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

