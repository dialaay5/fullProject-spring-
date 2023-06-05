package com.example.demo.controller;

import com.example.demo.model.ClassRoom;
import com.example.demo.model.ClientFaultException;
import com.example.demo.service.ClassRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/class")
@CrossOrigin
public class ClassRoomController {
    @Autowired
    ClassRoomService classRoomService;

    @GetMapping
    public ResponseEntity get() {
        try {
            List<ClassRoom> list = classRoomService.getAllClassRooms();
            return new ResponseEntity<List<ClassRoom>>(list, HttpStatus.OK);
        }
        catch (Exception e) {
            return new ResponseEntity<String>("{ \"Error\": \"" + e.toString() + "\" }",
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity getById(@PathVariable Integer id) {
            ClassRoom result = classRoomService.getClassRoomById(id);
            if (result != null) {
                return new ResponseEntity<ClassRoom>(result, HttpStatus.OK);
            }
            return new ResponseEntity<String>("{ \"Warning\": \"not found classRoom with Id " + id + "\" }",
                    HttpStatus.NOT_FOUND);
    }

    @PostMapping()
    public ResponseEntity post(@RequestBody ClassRoom classRoom) {
        try {
            ClassRoom postClassRoom = classRoomService.createClassRoom(classRoom);
                return new ResponseEntity<ClassRoom>(postClassRoom, HttpStatus.CREATED);
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
    public ResponseEntity put(@PathVariable Integer id, @RequestBody ClassRoom classRoom) {
        try {
            classRoomService.updateClassRoom(classRoom, id);
            return new ResponseEntity<ClassRoom>(classRoom, HttpStatus.OK);
        }
        catch (ClientFaultException e){
            return new ResponseEntity<String>("{ \"Client Error\": \"" +e.toString() + "\" }",
                    HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<String>("{ \"Error\": \"" + e.toString() + "\" }",
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @DeleteMapping(value = "/{id}")
    public ResponseEntity<String> delete(@PathVariable Integer id) {
        try {
            ClassRoom classRoomById = classRoomService.getClassRoomById(id);
            if (classRoomById != null) {
                classRoomService.deleteClassRoom(id);
                return new ResponseEntity<String>("{ \"Result\": \"classRoom deleted\" }", HttpStatus.OK);
            }
            return new ResponseEntity<String>("{ \"Warning\": \"not found classRoom with this Id = " + id + "\" }",
                    HttpStatus.NOT_FOUND);
        }
        catch (ClientFaultException e) {
            return new ResponseEntity<String>("{ \"Client Error\": \"" +e.toString() + "\" }",
                            HttpStatus.BAD_REQUEST);
        }
         catch(Exception e){
            return new ResponseEntity<String>("{ \"Error\": \"" + e.toString() + "\" }",
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}



