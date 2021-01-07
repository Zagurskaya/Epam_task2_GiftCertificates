package com.epam.esm.web.controller.impl;

import com.epam.esm.TagService;
import com.epam.esm.web.controller.TagController;
import com.epam.esm.model.TagDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;

@Controller
public class TagControllerImpl implements TagController {

    private TagService tagService;

    @Autowired
    public void setTagService(TagService tagService) {
        this.tagService = tagService;
    }

    @Override
    public Long create(TagDTO tag) {
        Assert.hasLength(tag.getName(), "name is mandatory field");
        Assert.isTrue(tag.getName().length() <= 100, "name max length 100");

        return tagService.create(tag);
    }

    @Override
    public TagDTO getTagById(Long id) {
        Assert.notNull(id, "id is mandatory field");
        Assert.isTrue(id >= 0, "id less than zero");

        return tagService.findById(id);
    }

    @Override
    public void delete(Long id) {
        Assert.notNull(id, "id is mandatory field");
        Assert.isTrue(id >= 0, "id less than zero");

        tagService.delete(id);
    }
}
