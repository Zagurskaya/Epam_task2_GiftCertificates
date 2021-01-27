package com.epam.esm.impl;

import com.epam.esm.GiftCertificateRepository;
import com.epam.esm.GiftCertificateService;
import com.epam.esm.TagRepository;
import com.epam.esm.converter.GiftCertificateConverter;
import com.epam.esm.converter.TagConverter;
import com.epam.esm.model.GiftCertificate;
import com.epam.esm.model.GiftCertificateDTO;
import com.epam.esm.model.Tag;
import com.epam.esm.model.TagDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class GiftCertificateServiceImpl implements GiftCertificateService {

    private final GiftCertificateRepository giftCertificateRepository;
    private final GiftCertificateConverter giftCertificateConverter;
    private final TagRepository tagRepository;
    private final TagConverter tagConverter;

    @Autowired
    public GiftCertificateServiceImpl(
            GiftCertificateRepository giftCertificateRepository,
            GiftCertificateConverter giftCertificateConverter,
            TagRepository tagRepository,
            TagConverter tagConverter
    ) {
        this.giftCertificateRepository = giftCertificateRepository;
        this.giftCertificateConverter = giftCertificateConverter;
        this.tagRepository = tagRepository;
        this.tagConverter = tagConverter;
    }

    @Override
    @Transactional(readOnly = true)
    public GiftCertificateDTO findById(Long id) {
        GiftCertificate giftCertificate = giftCertificateRepository.findById(id);
        List<Tag> tags = tagRepository.findListTagsByCertificateId(id);

        GiftCertificateDTO giftCertificateDTO = giftCertificateConverter.toDTO(giftCertificate);
        List<TagDTO> tagDTOList = tags.stream().map(tagConverter::toDTO).collect(Collectors.toList());
        giftCertificateDTO.setTags(tagDTOList);
        return giftCertificateDTO;
    }

    @Override
    @Transactional
    public Long create(GiftCertificateDTO giftCertificateDTO) {
        GiftCertificate giftCertificate = giftCertificateConverter.toEntity(giftCertificateDTO);
        giftCertificate.setCreationDate(LocalDateTime.now());
        giftCertificate.setLastUpdateDate(LocalDateTime.now());
        Long id = giftCertificateRepository.create(giftCertificate);
        List<TagDTO> tagDTOList = giftCertificateDTO.getTags();
        tagDTOList.forEach(tagDTO -> {
            Optional<Tag> tagByName = tagRepository.findByName(tagDTO.getName());
            if (!tagByName.isPresent()) {
                Long tagId = tagRepository.create(tagConverter.toEntity(tagDTO));
                tagRepository.createConnectionBetweenTagAndGiftCertificate(tagId, id);
            } else {
                tagRepository.createConnectionBetweenTagAndGiftCertificate(tagByName.get().getId(), id);
            }
        });
        return id;
    }

    @Override
    @Transactional(readOnly = true)
    public List<GiftCertificateDTO> findAll() {
        List<GiftCertificate> giftCertificates = giftCertificateRepository.findAll();
        List<GiftCertificateDTO> giftCertificateDTOS = giftCertificates.stream()
                .map(giftCertificateConverter::toDTO).collect(Collectors.toList());
        giftCertificateDTOS.forEach(giftCertificateDTO -> {
            List<Tag> tags = tagRepository.findListTagsByCertificateId(giftCertificateDTO.getId());
            List<TagDTO> tagDTOList = tags.stream().map(tagConverter::toDTO).collect(Collectors.toList());
            giftCertificateDTO.setTags(tagDTOList);
        });
        return giftCertificateDTOS;
    }

    @Override
    @Transactional
    public boolean update(GiftCertificateDTO giftCertificateDTO) {
        boolean result;

        GiftCertificate updateGiftCertificate = giftCertificateConverter.toEntity(giftCertificateDTO);
        updateGiftCertificate.setLastUpdateDate(LocalDateTime.now());
        result = giftCertificateRepository.update(updateGiftCertificate);

        List<TagDTO> tagDTOList = giftCertificateDTO.getTags();
        List<Tag> tagListOld = tagRepository.findListTagsByCertificateId(giftCertificateDTO.getId());
        updateTagListForGiftCertificate(tagDTOList, tagListOld, updateGiftCertificate);
        return result;
    }

    @Override
    @Transactional
    public boolean delete(Long id) {
        boolean result;
        List<Tag> tagList = tagRepository.findListTagsByCertificateId(id);
        tagList.forEach(tag -> tagRepository.deleteConnectionBetweenTagAndGiftCertificate(tag.getId(), id));
        result = giftCertificateRepository.delete(id);
        return result;
    }

    @Override
    public boolean updatePart(GiftCertificateDTO giftCertificateDTO) {
        boolean result;
        GiftCertificate updateGiftCertificate = giftCertificateConverter.toEntityPartFields(giftCertificateDTO);
        updateGiftCertificate.setLastUpdateDate(LocalDateTime.now());
        result = giftCertificateRepository.updatePart(updateGiftCertificate);

        List<TagDTO> tagDTOList = giftCertificateDTO.getTags();
        List<Tag> tagListOld = tagRepository.findListTagsByCertificateId(updateGiftCertificate.getId());
        updateTagListForGiftCertificate(tagDTOList, tagListOld, updateGiftCertificate);
        return result;
    }

    @Override
    public List<GiftCertificateDTO> findAllByFilter(Map<String, String> filter) {
        List<GiftCertificate> giftCertificates = giftCertificateRepository.findAllByFilter(filter);
        List<GiftCertificateDTO> giftCertificateDTOS = giftCertificates.stream()
                .map(giftCertificateConverter::toDTO).collect(Collectors.toList());
        giftCertificateDTOS.forEach(giftCertificateDTO -> {
            List<Tag> tags = tagRepository.findListTagsByCertificateId(giftCertificateDTO.getId());
            List<TagDTO> tagDTOList = tags.stream().map(tagConverter::toDTO).collect(Collectors.toList());
            giftCertificateDTO.setTags(tagDTOList);
        });
        return giftCertificateDTOS;
    }

    private void updateTagListForGiftCertificate(List<TagDTO> tagDTOList, List<Tag> oldTagList, GiftCertificate giftCertificate) {

        List<TagDTO> updateTagDTOList = tagDTOList.stream().filter(task -> task.getId() != null).collect(Collectors.toList());
        List<TagDTO> createTagList = tagDTOList.stream().filter(task -> task.getId() == null).collect(Collectors.toList());

        List<Tag> deleteTagList = new ArrayList<>(oldTagList);
        deleteTagList.removeAll(updateTagDTOList.stream().map(tagConverter::toEntity).collect(Collectors.toList()));

        List<TagDTO> addTagList = new ArrayList<>(tagDTOList);
        addTagList.removeAll(createTagList);
        addTagList.removeAll(oldTagList.stream().map(tagConverter::toDTO).collect(Collectors.toList()));

        createTagList.forEach(tagDTO -> {
            Optional<Tag> tag = tagRepository.findByName(tagDTO.getName());
            if (!tag.isPresent()) {
                Long newTagId = tagRepository.create(tagConverter.toEntity(tagDTO));
                tagRepository.createConnectionBetweenTagAndGiftCertificate(newTagId, giftCertificate.getId());
            } else {
                tagRepository.createConnectionBetweenTagAndGiftCertificate(tag.get().getId(), giftCertificate.getId());
            }
        });
        addTagList.forEach(tagDTO -> tagRepository.createConnectionBetweenTagAndGiftCertificate(tagDTO.getId(), giftCertificate.getId()));

        deleteTagList.forEach(tag -> tagRepository.deleteConnectionBetweenTagAndGiftCertificate(tag.getId(), giftCertificate.getId()));
    }
}