package com.epam.esm.controller;

import com.epam.esm.TagService;
import com.epam.esm.model.TagDTO;
import com.epam.esm.response.IdResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class TagController {

    private final TagService tagService;

    @Autowired
    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    @GetMapping(value = "/tags")
    public ResponseEntity<List<TagDTO>> getTags() {
        List<TagDTO> tags = tagService.findAll();
        return new ResponseEntity<>(tags, HttpStatus.OK);
    }

    @GetMapping(value = "/tags/{id}")
    public ResponseEntity<TagDTO> getTagById(@PathVariable("id") long id) {
        return new ResponseEntity<>(tagService.findById(id), HttpStatus.OK);
    }

    @PostMapping(value = "/tags")
    public ResponseEntity<IdResponse> createTag(@RequestBody TagDTO tag) {
        Long id = tagService.create(tag);
        IdResponse idResponse = new IdResponse(id);
        return new ResponseEntity<>(idResponse, HttpStatus.CREATED);
    }

    @DeleteMapping(value = "/tags/{id}")
    public ResponseEntity deleteTag(@PathVariable("id") long id) {
        tagService.delete(id);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}