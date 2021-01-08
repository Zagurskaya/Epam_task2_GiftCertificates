package com.epam.esm.web.controller;

import com.epam.esm.TagService;
import com.epam.esm.exception.ServiceException;
import com.epam.esm.model.TagDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class TagController {

    private TagService tagService;

    @Autowired
    public void setTagService(TagService tagService) {
        this.tagService = tagService;
    }

    @GetMapping(value = "/tags")
    public List<TagDTO> getTags() throws ServiceException {
        return tagService.findAll();
    }

    @GetMapping(value = "/tag/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TagDTO> getTagById(@PathVariable("id") long id) throws ServiceException {
        return new ResponseEntity(tagService.findById(id), HttpStatus.OK);
    }

//    @GetMapping(value = "/tag/{id}")
//    public TagDTO getTagById(@PathVariable("id") long id) throws ServiceException {
//        return tagService.findById(id);
//    }

    @PostMapping(value = "/tag")
    public ResponseEntity createTag(@RequestBody TagDTO tag) throws ServiceException {
        tagService.create(tag);
        return new ResponseEntity(HttpStatus.OK);
    }

    @PutMapping(value = "/tag/{id}")
    public ResponseEntity updateTag(@PathVariable("id") long id, @RequestBody TagDTO updateTag) throws ServiceException {
        TagDTO tag = tagService.findById(id);
        if (tag == null) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        tag.setName(updateTag.getName());
        tagService.update(tag);
        return new ResponseEntity(HttpStatus.OK);
    }

    @DeleteMapping(value = "/tag/{id}")
    public ResponseEntity deleteTag(@PathVariable("id") long id) throws ServiceException {
        TagDTO tag = tagService.findById(id);
        if (tag == null) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        tagService.delete(id);
        return new ResponseEntity(HttpStatus.OK);
    }
}