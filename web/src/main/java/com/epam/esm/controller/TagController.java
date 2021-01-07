package com.epam.esm.controller;

import com.epam.esm.model.TagDTO;

public interface TagController {

    Long create(TagDTO tag);

    TagDTO getTagById(Long id);

    void delete(Long id);
}
