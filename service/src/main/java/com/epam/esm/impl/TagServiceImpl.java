package com.epam.esm.impl;

import com.epam.esm.TagRepository;
import com.epam.esm.TagService;
import com.epam.esm.TagTestRepository;
import com.epam.esm.connection.ConnectionHandler;
import com.epam.esm.converter.TagConverter;
import com.epam.esm.model.Tag;
import com.epam.esm.model.TagDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;
    private final TagTestRepository tagTestRepository;
    private final TagConverter tagConverter;
    private final ConnectionHandler connectionHandler;

    @Autowired
    public TagServiceImpl(
            TagRepository tagRepository,
            TagTestRepository tagTestRepository,
            TagConverter tagConverter,
            ConnectionHandler connectionHandler
    ) {
        this.tagRepository = tagRepository;
        this.tagTestRepository = tagTestRepository;
        this.tagConverter = tagConverter;
        this.connectionHandler = connectionHandler;
    }

    @Override
    public TagDTO findById(Long id) {
        Tag tag = tagTestRepository.findById(id);
        return tagConverter.toDTO(tag);
    }

    @Override
    public Long create(TagDTO tagDTO) {
        Tag tag = tagConverter.toEntity(tagDTO);
        Long id = tagTestRepository.create(tag);
        return id;
    }

    @Override
    public List<TagDTO> findAll() {
        List<Tag> tags = tagTestRepository.findAll();
        List<TagDTO> tagDTOs = tags.stream().map(tagConverter::toDTO).collect(Collectors.toList());
        return tagDTOs;
    }

    @Override
    public boolean delete(Long id) {
        Tag tag = tagTestRepository.findById(id);
        if (tag == null) {
            return false;
        }
        return tagTestRepository.delete(id);
    }
}