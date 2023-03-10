package com.example.chatapplication.controller;


import com.example.chatapplication.dto.query.QueryDto;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/test")
public class TestController {

    @GetMapping("")
    public String test(@ParameterObject QueryDto queryDto){

        return "Hello TriMai";
    }
}
