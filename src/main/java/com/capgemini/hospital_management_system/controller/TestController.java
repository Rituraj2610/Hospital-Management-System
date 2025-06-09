package com.capgemini.hospital_management_system.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test123")
public class TestController {
    @GetMapping
    public String m123(){
        return "I am batman123";
    }
}
