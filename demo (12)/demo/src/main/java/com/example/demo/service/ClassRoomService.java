package com.example.demo.service;

import com.example.demo.model.ClassRoom;
import com.example.demo.model.ClassRoomType;
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
    public String createClassRoom(ClassRoom classRoom) {
        return classRoomRepository.createClassRoom(classRoom);
    }

    @Override
    public String updateClassRoom(ClassRoom classRoom, Integer id) throws Exception {
        if (classRoom.getClassRoomType().equals(ClassRoomType.EXTERNAL) && classRoom.getNumberOfStudents() >= maxstudents){
            return ("{\"Warning\": \"Cannot change the class room type because there are more than 20 students\" }");
        }
        String result = classRoomRepository.updateClassRoom(classRoom, id);
        return result;
    }


    @Override
    public String deleteClassRoom(Integer id) throws Exception {
        if(classRoomRepository.getClassRoomById(id).getNumberOfStudents() >= 1 ){
            return ("{\"Warning\": \"Cannot delete this classRoom\" }");
        }
        String result =  classRoomRepository.deleteClassRoom(id);
        return result;
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
