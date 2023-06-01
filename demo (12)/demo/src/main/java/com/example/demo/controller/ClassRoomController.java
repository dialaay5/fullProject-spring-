package com.example.demo.controller;

import com.example.demo.model.ClassRoom;
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
    public List<ClassRoom> get() {
        return classRoomService.getAllClassRooms();
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity getById(@PathVariable Integer id) {
        try {
            ClassRoom result = classRoomService.getClassRoomById(id);
            if (result != null) {
                return new ResponseEntity<ClassRoom>(result, HttpStatus.OK);
            }
            return new ResponseEntity<String>("{ \"Warning\": \"not found classRoom with Id " + id + "\" }",
                    HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<String>("{ \"Error\": \"" + e.toString() + "\" }",
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping()
    public ResponseEntity<String> post(@RequestBody ClassRoom classRoom) {
        try{
            String result = classRoomService.createClassRoom(classRoom);
            if (result == null) {
                return new ResponseEntity<String>("{ \"result\": \"classRoom created\" }", HttpStatus.CREATED);
            }
            return new ResponseEntity<String>(result, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<String>("{ \"Error\": \"" + e.toString() + "\" }",
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PutMapping(value = "/{id}")
    public ResponseEntity<String> put(@PathVariable Integer id, @RequestBody ClassRoom classRoom){
        try{
            String result = classRoomService.updateClassRoom(classRoom, id);
            if (result == null) {
                return new ResponseEntity<String>("{ \"result\": \"classRoom updated\" }", HttpStatus.OK);
            }
            return new ResponseEntity<String>(result, HttpStatus.BAD_REQUEST);
        }
        catch (Exception e) {
            return new ResponseEntity<String>("{ \"Error\": \"" + e.toString() + "\" }",
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



    @DeleteMapping(value = "/{id}")
    public ResponseEntity<String> delete(@PathVariable Integer id) {
        try {
            String result = classRoomService.deleteClassRoom(id);
            if (result == null) {
                return new ResponseEntity<String>("{ \"result\": \"classRoom deleted\" }", HttpStatus.OK);
            }
            return new ResponseEntity<String>(result, HttpStatus.BAD_REQUEST);

        } catch (Exception e) {
            return new ResponseEntity<String>("{ \"Error\": \"" + e.toString() + "\" }",
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}


