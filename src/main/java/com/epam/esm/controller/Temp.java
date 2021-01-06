package com.epam.esm.controller;

import com.epam.esm.entity.Tag;
import com.epam.esm.entity.User;
import com.epam.esm.exception.ServiceException;
import com.epam.esm.model.service.TagService;
import com.epam.esm.model.service.UserService;
import com.epam.esm.model.service.impl.TagServiceImpl;
import com.epam.esm.model.service.impl.UserServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class Temp {
    private UserService userService = new UserServiceImpl();
    private TagService tagService = new TagServiceImpl();

    @GetMapping(value = "/hello")
    @ResponseBody
    public String hello() {
        return "Hello";
    }

    @GetMapping(value = "/users")
    public ResponseEntity<List<User>> getUsers() throws ServiceException {
        return new ResponseEntity<>(userService.findAll(), HttpStatus.OK);
    }

    @GetMapping(value = "/tag")
    public Tag getTags() throws ServiceException {
        return tagService.findById(1L);
    }
}
