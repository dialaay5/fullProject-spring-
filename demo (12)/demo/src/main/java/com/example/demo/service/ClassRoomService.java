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
@Service
public class ClassRoomService implements IClassRoomService{
    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private ClassRoomRepository classRoomRepository;

    @Autowired
    private CacheRepositoryImpl cacheRepository;
    @Autowired
    private ObjectMapper objectMapper;

    @Value("${maxstudents}")
    private Integer maxstudents;

    @Value("${cache_on}")
    private Boolean cache_on;
    @Override
    public ClassRoom createClassRoom(ClassRoom classRoom) throws ClientFaultException {
        return classRoomRepository.createClassRoom(classRoom);
    }

    @Override
    public void updateClassRoom(ClassRoom classRoom, Integer id) throws ClientFaultException, JsonProcessingException {
        ClassRoom classRoomUpdate = classRoomRepository.getClassRoomById(id);
        if (classRoomUpdate != null) {
            if (classRoom.getClassRoomType().equals(ClassRoomType.EXTERNAL) && classRoom.getNumberOfStudents() >= maxstudents) {
                throw new NotIllegalChangeClassType("Cannot change the class room type because there are more than 20 students");
            }
            classRoomRepository.updateClassRoom(classRoom, id);
            if (cache_on && cacheRepository.isKeyExist("CLASS" + id.toString())) {
                cacheRepository.updateCacheEntity("CLASS" + id.toString(), objectMapper.writeValueAsString(classRoom));
            }
        }
    }


    @Override
    public void deleteClassRoom(Integer id) throws ClientFaultException {
        ClassRoom classRoomDelete = classRoomRepository.getClassRoomById(id);
        if (classRoomDelete != null) {
            if (classRoomRepository.getClassRoomById(id).getNumberOfStudents() >= 1) {
                throw new ClassNotEmptyException("Cannot delete this classRoom because its not empty ");
            }
            classRoomRepository.deleteClassRoom(id);
            if (cache_on && cacheRepository.isKeyExist("CLASS" + id.toString())) {
                cacheRepository.removeKey("CLASS" + id.toString());
            }
        }
    }

    @Override
    public List<ClassRoom> getAllClassRooms() {
        return classRoomRepository.getAllClassRooms();
    }

    @Override
    public ClassRoom getClassRoomById(Integer id) {
        try {

            if (cache_on && cacheRepository.isKeyExist("CLASS" + id.toString())) {
                System.out.println("is Key Exist? " + cacheRepository.isKeyExist("CLASS" + id.toString()));
                String classRoom = cacheRepository.getCacheEntity("CLASS" + id.toString());
                System.out.println("reading from cache " + classRoom);
                return objectMapper.readValue(classRoom, ClassRoom.class);
            }

            ClassRoom result = classRoomRepository.getClassRoomById(id);
            if(result != null) {
                if (cache_on) {
                    cacheRepository.createCacheEntity("CLASS" + id.toString(), objectMapper.writeValueAsString(result));
                }
            }
            return result;

        } catch (JsonProcessingException e) {
            System.out.println(e);
            throw new IllegalStateException("cannot write json of classRoom");
        }
    }
    }

