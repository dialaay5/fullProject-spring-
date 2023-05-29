package com.example.demo.repository;

import com.example.demo.model.ClassRoom;
import com.example.demo.model.ClassRoomMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public class ClassRoomRepository implements IClassRoomRepository{

    @Autowired
    private StudentRepository studentRepository;
    private static final String CLASSROOM_TABLE_NAME = "classRoom";

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public String createClassRoom(ClassRoom classRoom) {
        try {
            String query = String.format("INSERT INTO %s (numberOfStudents, classAvg, classRoomType) VALUES (?, ?, ?)", CLASSROOM_TABLE_NAME);
            jdbcTemplate.update(query, classRoom.getNumberOfStudents(), classRoom.getClassAvg(),classRoom.getClassRoomType().name());
            return null;
        } catch (Exception e) {
            return "{\"Error\": \"" + e.toString() + "\" }";
        }
    }

    @Override
    public String updateClassRoom(ClassRoom classRoom, Integer id) {
        try{
            String query = String.format("UPDATE %s SET numberOfStudents=?, classAvg=?, classRoomType=?  WHERE id= ?", CLASSROOM_TABLE_NAME);
            jdbcTemplate.update(query, classRoom.getNumberOfStudents(), classRoom.getClassAvg(),classRoom.getClassRoomType().name(), id);
            return null;
        } catch (Exception e) {
            return "{\"Error\": \"" + e.toString() + "\" }";
        }
    }

    @Override
    public String deleteClassRoom(Integer id) {
        try{
            String query = String.format("DELETE FROM %s WHERE id= ?", CLASSROOM_TABLE_NAME);
            jdbcTemplate.update(query, id);
            return null;
        }
        catch (Exception e) {
            return "{\"Error\": \"" + e.toString() + "\" }";
        }
    }

    @Override
    public List<ClassRoom> getAllClassRooms() {
        String query = String.format("Select * from %s", CLASSROOM_TABLE_NAME);
        return jdbcTemplate.query(query, new ClassRoomMapper());
    }

    @Override
    public ClassRoom getClassRoomById(Integer id) {
        String query = String.format("Select * from %s where id=?", CLASSROOM_TABLE_NAME);
        try
        {
            return jdbcTemplate.queryForObject(query, new ClassRoomMapper(), id);
        }
        catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public void synchronizationClassAvg_countOfStudents(Integer id) {
        String query = String.format("UPDATE %s SET classAvg= (Select avg(avgGrade) from student where class_id = ?), numberOfStudents= (Select count(*) from student where class_id = ?)  WHERE id= ?", CLASSROOM_TABLE_NAME);
        jdbcTemplate.update(query, id, id, id);
    }
}



