package com.epam.esm.mapper;

import com.epam.esm.model.TagDTO;
import com.epam.esm.request.TagRequest;
import org.springframework.stereotype.Component;

@Component
public class TagMapper {

    public TagDTO toDTO(TagRequest tag) {
        TagDTO tagDTO = new TagDTO();
        tagDTO.setName(tag.getName());
        return tagDTO;
    }
}
