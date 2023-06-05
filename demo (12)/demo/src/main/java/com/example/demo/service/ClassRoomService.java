package com.example.demo.service;

import com.example.demo.model.*;
import com.example.demo.repository.ClassRoomRepository;
import com.example.demo.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class ClassRoomService implements IClassRoomService{
    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private ClassRoomRepository classRoomRepository;
    @Value("${maxstudents}")
    private Integer maxstudents;
    @Override
    public ClassRoom createClassRoom(ClassRoom classRoom) throws ClientFaultException {
        return classRoomRepository.createClassRoom(classRoom);
    }

    @Override
    public void updateClassRoom(ClassRoom classRoom, Integer id) throws ClientFaultException {
        if (classRoom.getClassRoomType().equals(ClassRoomType.EXTERNAL) && classRoom.getNumberOfStudents() >= maxstudents){
            throw new NotIllegalChangeClassType("Cannot change the class room type because there are more than 20 students");
        }
        classRoomRepository.updateClassRoom(classRoom, id);
    }


    @Override
    public void deleteClassRoom(Integer id) throws ClientFaultException {
        if(classRoomRepository.getClassRoomById(id).getNumberOfStudents() >= 1 ){
            throw new ClassNotEmptyException("Cannot delete this classRoom because its not empty ");
        }
        classRoomRepository.deleteClassRoom(id);
    }

    @Override
    public List<ClassRoom> getAllClassRooms() {
        return classRoomRepository.getAllClassRooms();
    }

    @Override
    public ClassRoom getClassRoomById(Integer id) {
        return classRoomRepository.getClassRoomById(id);
    }
}
