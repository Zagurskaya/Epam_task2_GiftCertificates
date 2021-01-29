package com.epam.esm.impl;

import com.epam.esm.GiftCertificateTagRepository;
import com.epam.esm.TagRepository;
import com.epam.esm.TagService;
import com.epam.esm.converter.TagConverter;
import com.epam.esm.exception.EmptyFieldException;
import com.epam.esm.exception.EntityAlreadyExistException;
import com.epam.esm.exception.EntityNotFoundException;
import com.epam.esm.model.Tag;
import com.epam.esm.model.TagDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;
    private final TagConverter tagConverter;
    private final GiftCertificateTagRepository giftCertificateTagRepository;


    @Autowired
    public TagServiceImpl(
            TagRepository tagRepository,
            TagConverter tagConverter,
            GiftCertificateTagRepository giftCertificateTagRepository) {
        this.tagRepository = tagRepository;
        this.tagConverter = tagConverter;
        this.giftCertificateTagRepository = giftCertificateTagRepository;
    }

    @Override
    public TagDTO findById(Long id) {
        try {
            Tag tag = tagRepository.findById(id);
            return tagConverter.toDTO(tag);
        } catch (
                EmptyResultDataAccessException exception) {
            throw new EntityNotFoundException("Tag not found with id " + id);
        }
    }

    @Override
    @Transactional
    public Long create(TagDTO tagDTO) {
        try {
            Tag tag = tagConverter.toEntity(tagDTO);
            return tagRepository.create(tag);
        } catch (
                DuplicateKeyException exception) {
            throw new EntityAlreadyExistException("Tag found with name " + tagDTO.getName());
        } catch (
                DataIntegrityViolationException exception) {
            throw new EmptyFieldException("Tag has empty field ");
        }
    }

    @Override
    public List<TagDTO> findAll() {
        List<Tag> tags = tagRepository.findAll();
        return tags.stream().map(tagConverter::toDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public boolean delete(Long id) {
        boolean result;
        List<Long> certificateIdList = giftCertificateTagRepository.findListCertificateIdByTagId(id);
        certificateIdList.forEach(certificateId -> giftCertificateTagRepository.deleteRelationBetweenTagAndGiftCertificate(id, certificateId));
        result = tagRepository.delete(id);
        return result;
    }
}