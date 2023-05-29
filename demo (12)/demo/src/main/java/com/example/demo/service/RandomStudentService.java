package com.example.demo.service;


import com.example.demo.model.RandomStudentResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;


@FeignClient(name = "randomStudentService", url = "${randomStudent.url}")
public interface RandomStudentService {

    @GetMapping
    RandomStudentResponse getRandomStudent();
}
