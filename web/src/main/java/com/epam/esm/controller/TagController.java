package com.epam.esm.controller;

import com.epam.esm.TagService;
import com.epam.esm.mapper.TagMapper;
import com.epam.esm.model.TagDTO;
import com.epam.esm.response.IdResponse;
import com.epam.esm.request.TagRequest;
import com.epam.esm.validator.TagValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Validated
public class TagController {

    private final TagService tagService;
    private final TagValidator tagValidator;
    private final TagMapper tagMapper;

    @Autowired
    public TagController(TagService tagService, TagValidator tagValidator, TagMapper tagMapper) {
        this.tagService = tagService;
        this.tagValidator = tagValidator;
        this.tagMapper = tagMapper;
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
    public ResponseEntity<IdResponse> createTag(@RequestBody TagRequest tag) {
        tagValidator.createValidate(tag);
        Long id = tagService.create(tagMapper.toDTO(tag));
        IdResponse idResponse = new IdResponse(id);
        return new ResponseEntity<>(idResponse, HttpStatus.CREATED);
    }

    @DeleteMapping(value = "/tags/{id}")
    public ResponseEntity deleteTag(@PathVariable("id") long id) {
        tagService.delete(id);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}