package com.example.demo.repository;

import com.example.demo.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class StudentRepository implements IStudentRepository {
    private static final String STUDENT_TABLE_NAME = "student";

    @Autowired
    private DataSource dataSource;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public Student createStudent(Student student) throws ClientFaultException {
        try {
        NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);

        String queryNamedParam = String.format("INSERT INTO %s (lastName, firstName, avgGrade, gender, class_id) VALUES (:lastName, :firstName, :avgGrade, :gender, :class_id)", STUDENT_TABLE_NAME);


        Map<String, Object> params = new HashMap<>();
        params.put("lastName", student.getLastName());
        params.put("firstName", student.getFirstName());
        params.put("avgGrade", student.getAvgGrade());
        params.put("gender", student.getGender().name());
        params.put("class_id", student.getClass_id());

        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource(params);

        GeneratedKeyHolder generatedKeyHolder = new GeneratedKeyHolder();

        namedParameterJdbcTemplate.update(queryNamedParam, mapSqlParameterSource, generatedKeyHolder);

        Integer id = (Integer) generatedKeyHolder.getKeys().get("id");

        student.setId(id);
        return student;

    }  catch (Exception e) {
        throw new AllDataAlreadyExistException("There is not enough data to create a new student");
    }
}

    //יצאתי מנקודת הנחה שאי אפשר להעביר התלמיד מכיתה לכיתה אחרת
    @Override
    public void updateStudent(Student student, Integer id) {
            String query = String.format("UPDATE %s SET lastName=?, firstName=?, avgGrade=?, gender=?, class_id=? WHERE id= ?", STUDENT_TABLE_NAME);
            jdbcTemplate.update(query, student.getLastName(), student.getFirstName(),
                    student.getAvgGrade(), student.getGender().name(), student.getClass_id(), id);
    }

    @Override
    public void deleteStudent(Integer id) {
            String query = String.format("DELETE FROM %s WHERE id= ?", STUDENT_TABLE_NAME);
            jdbcTemplate.update(query, id);
    }

    @Override
    public List<Student> getAllStudents() {
        String query = String.format("Select * from %s ORDER BY id ASC ", STUDENT_TABLE_NAME);
        return jdbcTemplate.query(query, new StudentMapper());
    }

    @Override
    public Student getStudentById(Integer id) {
        String query = String.format("Select * from %s where id=? ORDER BY id ASC ", STUDENT_TABLE_NAME);
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
        return jdbcTemplate.query(query, new StudentMapper());
    }

    @Override
    public List<Student> getStudentsByClassRoomId(Integer class_id) {
        String query = String.format("SELECT s.* FROM %s s JOIN classRoom c ON s.class_id = c.id WHERE c.id = ?;", STUDENT_TABLE_NAME);
        return jdbcTemplate.query(query, new StudentMapper(), class_id);
    }
}

