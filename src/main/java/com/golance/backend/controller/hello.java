package com.golance.backend.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class hello {

    @GetMapping("/")
    public String hello() {
        return "Hello, Spring Boot is working!";
    }
}
