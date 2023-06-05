package com.example.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class WebController {
    @RequestMapping(value = "/index")
    @GetMapping
    public ModelAndView home()
    {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("index"); //השם של הקובץ שרוצים להציג אותו (index.html)
        return modelAndView;
    }
}

