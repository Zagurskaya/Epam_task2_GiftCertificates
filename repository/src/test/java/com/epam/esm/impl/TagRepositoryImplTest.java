package com.epam.esm.impl;

import com.epam.esm.TagRepository;
import com.epam.esm.config.RepositoryTestConfig;
import com.epam.esm.exception.EntityNotFoundException;
import com.epam.esm.model.Tag;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.Assert.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = RepositoryTestConfig.class)
class TagRepositoryImplTest {

    @Autowired
    private TagRepository tagRepository;

    @Test
    void createTest() {
        Tag tag = new Tag();
        tag.setName("tag1");
        Long id = tagRepository.create(tag);
        Tag newTag = tagRepository.findById(id);
        assertNotEquals(java.util.Optional.ofNullable(id), 0L);
        assertEquals(tag.getName(), newTag.getName());

    }

    @Test
    void findAllTest() {
        List<Tag> tags = tagRepository.findAll();
        assertNotNull(tags.size());
    }

    @Test
    void findByIdTest() {
        Tag tag = tagRepository.findById(1L);
        assertNotNull(tag);
    }

    @Test
    void findByNameTest() {
        String name = "belita";
        Tag tag = tagRepository.findByName(name).get();
        assertEquals(tag.getName(), name);
    }

    @Test
    void deleteTest() {
        Tag tag = new Tag();
        tag.setName("newTag");
        Long id = tagRepository.create(tag);
        boolean result = tagRepository.delete(id);
        assertTrue(result);
        Assertions.assertThrows(EntityNotFoundException.class, () -> {
            tagRepository.findById(id);
        });
    }
}