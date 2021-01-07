package com.epam.esm.converter;

import com.epam.esm.model.Tag;
import com.epam.esm.model.TagDTO;

public interface TagConverter {

    TagDTO toDTO(Tag tag);

    Tag toEntity(TagDTO tagDTO);

}