package com.epam.esm.controller;

import com.epam.esm.entity.Tag;
import com.epam.esm.exception.CommandException;
import com.epam.esm.exception.ServiceException;
import com.epam.esm.model.service.TagService;
import com.epam.esm.model.service.impl.TagServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class TagController {
    private TagService tagService = new TagServiceImpl();

    @GetMapping(value = "/tags")
    public List<Tag> getTags() throws ServiceException {
        return tagService.findAll();
    }

    @GetMapping(value = "/tag/{id}")
    public Tag getTagById(@PathVariable("id") long id) throws ServiceException {
        return tagService.findById(id);
    }

    @PostMapping(value = "/tag")
    public ResponseEntity createTag(@RequestBody Tag tag) throws ServiceException, CommandException {
        tagService.create(tag);
        return new ResponseEntity(HttpStatus.OK);
    }

    @PutMapping(value = "/tag/{id}")
    public ResponseEntity updateTag(@PathVariable("id") long id, @RequestBody Tag updateTag) throws ServiceException, CommandException {
        Tag tag = tagService.findById(id);
        if (tag == null) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        tag.setName(updateTag.getName());
        tagService.update(tag);
        return new ResponseEntity(HttpStatus.OK);
    }

    @DeleteMapping(value = "/tag/{id}")
    public ResponseEntity deleteTag(@PathVariable("id") long id) throws ServiceException, CommandException {
        Tag tag = tagService.findById(id);
        if (tag == null) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        tagService.delete(tag);
        return new ResponseEntity(HttpStatus.OK);
    }
}
