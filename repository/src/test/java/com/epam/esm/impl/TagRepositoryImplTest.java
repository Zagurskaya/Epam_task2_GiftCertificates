package com.epam.esm.impl;

import com.epam.esm.TagRepository;
import com.epam.esm.config.RepositoryTestConfig;
import com.epam.esm.model.Tag;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
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

    private static Long createId;

    @Test
    @Order(1)
    void createTest() {
        Tag tag = new Tag();
        tag.setName("tag1");
        this.createId = tagRepository.create(tag);
        assertNotEquals(java.util.Optional.ofNullable(createId), 0L);
    }

    @Test
    @Order(2)
    void findAllTest() {
        List<Tag> tags = tagRepository.findAll();
        assertNotNull(tags.size());
    }

    @Test
    @Order(3)
    void findByIdTest() {
        Tag tag = tagRepository.findById(createId);
        assertNotNull(tag);
    }

    @Test
    @Order(4)
    void findByNameTest() {
        Tag tag = tagRepository.findByName("tag1").get();
        assertTrue(tag.getId() == createId);
    }
//
//    @Test
//    @Order(5)
//    void deleteTest() {
//        boolean result = tagRepository.delete(createId);
//        assertTrue(result);
//    }
}