package com.epam.esm.impl;

import com.epam.esm.TagRepository;
import com.epam.esm.TagService;
import com.epam.esm.converter.TagConverter;
import com.epam.esm.exception.EntityNotFoundException;
import com.epam.esm.model.Tag;
import com.epam.esm.model.TagDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;
    private final TagConverter tagConverter;

    @Autowired
    public TagServiceImpl(
            TagRepository tagRepository,
            TagConverter tagConverter
    ) {
        this.tagRepository = tagRepository;
        this.tagConverter = tagConverter;
    }

    @Override
    public TagDTO findById(Long id) {
        Tag tag = tagRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Tag not found with id " + id));
        return tagConverter.toDTO(tag);
    }

    @Override
    public Long create(TagDTO tagDTO) {
        Tag tag = tagConverter.toEntity(tagDTO);
        return tagRepository.create(tag);
    }

    @Override
    public List<TagDTO> findAll() {
        List<Tag> tags = tagRepository.findAll();
        return tags.stream().map(tagConverter::toDTO).collect(Collectors.toList());
    }

    @Override
    public boolean delete(Long id) {
        Tag tag = tagRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Tag not found with id " + id));
        if (tag == null) {
            return false;
        }
        return tagRepository.delete(id);
    }
}