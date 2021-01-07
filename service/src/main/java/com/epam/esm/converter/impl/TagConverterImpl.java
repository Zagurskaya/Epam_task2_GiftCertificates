package com.epam.esm.converter.impl;

import com.epam.esm.converter.TagConverter;
import com.epam.esm.model.Tag;
import com.epam.esm.model.TagDTO;
import org.springframework.stereotype.Component;


@Component
public class TagConverterImpl implements TagConverter {

    @Override
    public TagDTO toDTO(Tag tag) {
        TagDTO tagDTO = new TagDTO();
        tagDTO.setId(tag.getId());
        tagDTO.setName(tag.getName());
        return tagDTO;
    }

    @Override
    public Tag toEntity(TagDTO tagDTO) {
        Tag tag = new Tag();
        tag.setId(tagDTO.getId());
        tag.setName(tagDTO.getName());
        return tag;
    }


}
