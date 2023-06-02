package com.example.demo;

import com.example.demo.model.ClassRoom;
import com.example.demo.model.ClassRoomType;
import com.example.demo.model.Student;
import com.example.demo.model.StudentGender;
import com.example.demo.repository.ClassRoomRepository;
import com.example.demo.repository.StudentRepository;
import com.example.demo.service.StudentService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;

@SpringBootApplication
@EnableFeignClients

public class DemoApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(DemoApplication.class, args);
	}

	@Bean
	CommandLineRunner commandLineRunner(JdbcTemplate jdbcTemplate, ClassRoomRepository classRoomRepository, StudentService studentService) {
		return args -> {
			jdbcTemplate.execute(
					"DROP TABLE IF EXISTS classRoom cascade;\n" +
							"DROP TABLE IF EXISTS student cascade;\n" +
							"CREATE TABLE classRoom ("+
							"    id SERIAL PRIMARY KEY,\n" +
							"    numberOfStudents int NOT NULL,\n" +
							"    classAvg float,\n" +
							"    classRoomType varchar(255) NOT NULL default 'REGULAR');\n" +
							"CREATE TABLE student (" +
							"    id SERIAL PRIMARY KEY,\n" +
							"    lastName varchar(255) NOT NULL default '',\n" +
							"    firstName varchar(255) NOT NULL default '',\n" +
							"    avgGrade float NOT NULL,\n" +
							"    gender varchar(255) NOT NULL default 'MALE',\n" +
							"    class_id int NOT NULL,\n" +
							"    FOREIGN KEY (class_id) REFERENCES classRoom(id));");

			classRoomRepository.createClassRoom(new ClassRoom(0,0,0.0f, ClassRoomType.EXTERNAL));
			classRoomRepository.createClassRoom(new ClassRoom(0,0,0.0f, ClassRoomType.REGULAR));
			classRoomRepository.createClassRoom(new ClassRoom(0,0,0.0f, ClassRoomType.EXTERNAL));
			classRoomRepository.createClassRoom(new ClassRoom(0,0,0.0f, ClassRoomType.REGULAR));

			studentService.createStudent(new Student(0,"Ayoub","Diala",100.0f,StudentGender.FEMALE,1));
			studentService.createStudent(new Student(0,"Abutbul","Tal",100.0f,StudentGender.MALE,2));
			studentService.createStudent(new Student(0,"Cohen","Yaniv",100.0f,StudentGender.MALE,3));
			studentService.createStudent(new Student(0,"Dassi","Dassi",100.0f,StudentGender.FEMALE,4));
			studentService.createStudent(new Student(0,"Levi","Danny",80.0f,StudentGender.MALE,1));





		};
	}

}

