package com.example.demo.repository;

import com.example.demo.model.AllDataAlreadyExistException;
import com.example.demo.model.ClassRoom;
import com.example.demo.model.ClassRoomMapper;
import com.example.demo.model.ClientFaultException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class ClassRoomRepository implements IClassRoomRepository{

    @Autowired
    private StudentRepository studentRepository;
    private static final String CLASSROOM_TABLE_NAME = "classRoom";

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private DataSource dataSource;

    @Override
    public ClassRoom createClassRoom(ClassRoom classRoom) throws ClientFaultException {
        try {
            NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);

            String queryNamedParam = String.format("INSERT INTO %s (numberOfStudents, classAvg, classRoomType) VALUES (:numberOfStudents, :classAvg, :classRoomType)", CLASSROOM_TABLE_NAME);


            Map<String, Object> params = new HashMap<>();
            params.put("numberOfStudents", classRoom.getNumberOfStudents());
            params.put("classAvg", classRoom.getClassAvg());
            params.put("classRoomType", classRoom.getClassRoomType().name());

            MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource(params);

            GeneratedKeyHolder generatedKeyHolder = new GeneratedKeyHolder();

            namedParameterJdbcTemplate.update(queryNamedParam, mapSqlParameterSource, generatedKeyHolder);

            Integer id = (Integer) generatedKeyHolder.getKeys().get("id");

            classRoom.setId(id);
            return classRoom;

        }  catch (Exception e) {
            throw new AllDataAlreadyExistException("There is not enough data to create a new class");
        }
    }


    @Override
    public void updateClassRoom(ClassRoom classRoom, Integer id) {
        String query = String.format("UPDATE %s SET numberOfStudents=?, classAvg=?, classRoomType=?  WHERE id= ?", CLASSROOM_TABLE_NAME);
        jdbcTemplate.update(query, classRoom.getNumberOfStudents(), classRoom.getClassAvg(), classRoom.getClassRoomType().name(), id);
    }

    @Override
    public void deleteClassRoom(Integer id) {
            String query = String.format("DELETE FROM %s WHERE id= ?", CLASSROOM_TABLE_NAME);
            jdbcTemplate.update(query, id);
    }

    @Override
    public List<ClassRoom> getAllClassRooms() {
        String query = String.format("Select * from %s ORDER BY id ASC ", CLASSROOM_TABLE_NAME);
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



