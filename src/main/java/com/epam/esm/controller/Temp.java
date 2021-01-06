package com.epam.esm.controller;


import com.epam.esm.model.service.TagService;
import com.epam.esm.model.service.impl.TagServiceImpl;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Temp {
    private TagService tagService = new TagServiceImpl();

    @GetMapping(value = "/hello")
    @ResponseBody
    public String hello() {
        return "Hello";
    }
}
