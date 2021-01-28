package com.epam.esm.impl;

import com.epam.esm.GiftCertificateTagRepository;
import com.epam.esm.TagRepository;
import com.epam.esm.TagService;
import com.epam.esm.converter.TagConverter;
import com.epam.esm.model.Tag;
import com.epam.esm.model.TagDTO;
import org.springframework.beans.factory.annotation.Autowired;
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
        Tag tag = tagRepository.findById(id);
        return tagConverter.toDTO(tag);
    }

    @Override
    @Transactional
    public Long create(TagDTO tagDTO) {
        Tag tag = tagConverter.toEntity(tagDTO);
        return tagRepository.create(tag);
    }

    @Override
    @Transactional(readOnly = true)
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