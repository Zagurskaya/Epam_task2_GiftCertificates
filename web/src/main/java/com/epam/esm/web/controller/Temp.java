package com.epam.esm.web.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Temp {
    @GetMapping(value = "/hello")
    public String hello() {
        return "Hello";
    }
}