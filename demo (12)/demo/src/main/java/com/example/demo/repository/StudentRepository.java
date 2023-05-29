package com.example.demo.repository;

import com.example.demo.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public class StudentRepository implements IStudentRepository {
    private static final String STUDENT_TABLE_NAME = "student";

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public String createStudent(Student student) {
        try {
            String query = String.format("INSERT INTO %s (lastName, firstName, avgGrade, gender, class_id) VALUES (?, ?, ?, ?, ?)", STUDENT_TABLE_NAME);
            jdbcTemplate.update(query, student.getLastName(), student.getFirstName(),
                    student.getAvgGrade(), student.getGender().name(), student.getClass_id());
            return null;
        } catch (Exception e) {
            return "{\"Error\": \"" + e.toString() + "\" }";
        }
    }

    //יצאתי מנקודת הנחה שאי אפשר להעביר התלמיד מכיתה לכיתה אחרת
    @Override
    public String updateStudent(Student student, Integer id) {
        try{
            String query = String.format("UPDATE %s SET lastName=?, firstName=?, avgGrade=?, gender=? WHERE id= ?", STUDENT_TABLE_NAME);
            jdbcTemplate.update(query, student.getLastName(), student.getFirstName(),
                    student.getAvgGrade(), student.getGender().name(), id);
            return null;
        } catch (Exception e) {
            return "{\"Error\": \"" + e.toString() + "\" }";
        }
    }

    @Override
    public String deleteStudent(Integer id) {
        try{
            String query = String.format("DELETE FROM %s WHERE id= ?", STUDENT_TABLE_NAME);
            jdbcTemplate.update(query, id);
            return null;
        } catch (Exception e) {
            return "{\"Error\": \"" + e.toString() + "\" }";
        }
    }

    @Override
    public List<Student> getAllStudents() {
        String query = String.format("Select * from %s", STUDENT_TABLE_NAME);
        return jdbcTemplate.query(query, new StudentMapper());
    }

    @Override
    public Student getStudentById(Integer id) {
        String query = String.format("Select * from %s where id=?", STUDENT_TABLE_NAME);
        try {
            return jdbcTemplate.queryForObject(query, new StudentMapper(), id);
        }
        catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public List<Student> getStudentsByClassRoomType() {
        String query = String.format("SELECT * FROM %s WHERE class_id IN (SELECT id FROM classRoom WHERE classRoomType= 'EXTERNAL') ORDER BY class_id", STUDENT_TABLE_NAME);
        try
        {
            return jdbcTemplate.query(query, new StudentMapper());
        }
        catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public List<Student> getStudentsByClassRoomId(Integer class_id) {
        String query = String.format("SELECT s.* FROM %s s JOIN classRoom c ON s.class_id = c.id WHERE c.id = ?;", STUDENT_TABLE_NAME);
        try
        {
            return jdbcTemplate.query(query, new StudentMapper(), class_id);
        }
        catch (EmptyResultDataAccessException e) {
            return null;
        }
    }
}

