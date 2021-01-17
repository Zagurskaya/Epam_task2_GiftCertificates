package com.epam.esm.impl;

import com.epam.esm.GiftCertificateRepository;
import com.epam.esm.GiftCertificateService;
import com.epam.esm.TagRepository;
import com.epam.esm.converter.GiftCertificateConverter;
import com.epam.esm.converter.TagConverter;
import com.epam.esm.exception.ServiceException;
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
    public GiftCertificateDTO findById(Long id) throws ServiceException {
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
        Long id = giftCertificateRepository.create(giftCertificate);
        List<TagDTO> tagDTOList = giftCertificateDTO.getTags();
        if (tagDTOList.size() != 0) {
            tagDTOList.forEach(tagDTO -> {
                Tag tagByName = tagRepository.findByName(tagDTO.getName());
                if (tagByName == null) {
                    Long tagId = tagRepository.create(tagConverter.toEntity(tagDTO));
                    tagRepository.createConnectionBetweenTagAndGiftCertificate(tagId, id);
                } else {
                    tagRepository.createConnectionBetweenTagAndGiftCertificate(tagByName.getId(), id);
                }
            });
        }
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
    @Transactional(readOnly = true)
    public GiftCertificateDTO findByName(String name) throws ServiceException {
        GiftCertificateDTO giftCertificateDTO = null;
        GiftCertificate giftCertificate = giftCertificateRepository.findByName(name);
        if (giftCertificate != null) {
            List<Tag> tags = tagRepository.findListTagsByCertificateId(giftCertificate.getId());
            giftCertificateDTO = giftCertificateConverter.toDTO(giftCertificate);
            List<TagDTO> tagDTOList = tags.stream().map(tagConverter::toDTO).collect(Collectors.toList());
            giftCertificateDTO.setTags(tagDTOList);
        }
        return giftCertificateDTO;
    }

    @Override
    @Transactional
    public boolean update(GiftCertificateDTO giftCertificateDTO) {
        boolean result;
        GiftCertificate giftCertificate = giftCertificateRepository.findById(giftCertificateDTO.getId());
        if (giftCertificate == null) {
            return false;
        } else {
            GiftCertificate updateGiftCertificate = new GiftCertificate();
            updateGiftCertificate.setId(giftCertificate.getId());
            updateGiftCertificate.setName(giftCertificateDTO.getName());
            updateGiftCertificate.setDescription(giftCertificateDTO.getDescription());
            updateGiftCertificate.setPrice(giftCertificateDTO.getPrice());
            updateGiftCertificate.setDuration(giftCertificateDTO.getDuration());
            updateGiftCertificate.setLastUpdateDate(LocalDateTime.now());
            updateGiftCertificate.setCreationDate(giftCertificate.getCreationDate());
            result = giftCertificateRepository.update(updateGiftCertificate);

            List<TagDTO> tagDTOList = giftCertificateDTO.getTags();
            if (tagDTOList.size() != 0) {
                List<Tag> oldTagList = tagRepository.findListTagsByCertificateId(giftCertificate.getId());
                List<TagDTO> updateTagDTOList = tagDTOList.stream().filter(task -> task.getId() != null).collect(Collectors.toList());
                List<TagDTO> createTagList = tagDTOList.stream().filter(task -> task.getId() == null).collect(Collectors.toList());

                List<Tag> deleteTagList = new ArrayList<>(oldTagList);
                deleteTagList.removeAll(updateTagDTOList.stream().map(tagConverter::toEntity).collect(Collectors.toList()));

                List<TagDTO> addTagList = new ArrayList<>(tagDTOList);
                addTagList.removeAll(createTagList);
                addTagList.removeAll(oldTagList.stream().map(tagConverter::toDTO).collect(Collectors.toList()));

                for (TagDTO tagDTO : createTagList) {
                    Tag tag = tagRepository.findByName(tagDTO.getName());
                    if (tag == null) {
                        Tag newTag = new Tag();
                        newTag.setName(tagDTO.getName());
                        Long newTagId = tagRepository.create(newTag);
                        tagRepository.createConnectionBetweenTagAndGiftCertificate(newTagId, giftCertificate.getId());
                    } else {
                        tagRepository.createConnectionBetweenTagAndGiftCertificate(tag.getId(), giftCertificate.getId());
                    }
                }
                addTagList.forEach(tagDTO -> tagRepository.createConnectionBetweenTagAndGiftCertificate(tagDTO.getId(), giftCertificate.getId()));

                deleteTagList.forEach(tag -> tagRepository.deleteConnectionBetweenTagAndGiftCertificate(tag.getId(), giftCertificate.getId()));
            }
        }
        return result;
    }

    @Override
    @Transactional
    public boolean delete(Long id) throws ServiceException {
        boolean result;
        GiftCertificate giftCertificate = giftCertificateRepository.findById(id);
        if (giftCertificate == null) {
            return false;
        }
        List<Tag> tagList = tagRepository.findListTagsByCertificateId(giftCertificate.getId());
        tagList.forEach(tag -> tagRepository.deleteConnectionBetweenTagAndGiftCertificate(tag.getId(), id));
        result = giftCertificateRepository.delete(id);
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public List<GiftCertificateDTO> findAllGiftCertificateListByTagName(String tagName) {
        List<GiftCertificate> giftCertificates = giftCertificateRepository.findAllByTagName(tagName);
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
    public boolean updatePart(GiftCertificateDTO giftCertificateDTO) {
        boolean result;
        GiftCertificate giftCertificate = giftCertificateRepository.findById(giftCertificateDTO.getId());
        if (giftCertificate == null) {
            return false;
        } else {
            GiftCertificate updateGiftCertificate = new GiftCertificate();
            updateGiftCertificate.setId(giftCertificate.getId());
            if (giftCertificateDTO.getName() != null) {
                updateGiftCertificate.setName(giftCertificateDTO.getName());
            }
            if (giftCertificateDTO.getDescription() != null) {
                updateGiftCertificate.setDescription(giftCertificateDTO.getDescription());
            }
            if (giftCertificateDTO.getPrice() != null) {
                updateGiftCertificate.setPrice(giftCertificateDTO.getPrice());
            }
            if (giftCertificateDTO.getDuration() != null) {
                updateGiftCertificate.setDuration(giftCertificateDTO.getDuration());
            }
            updateGiftCertificate.setLastUpdateDate(LocalDateTime.now());
            result = giftCertificateRepository.updatePart(updateGiftCertificate);

            List<TagDTO> tagDTOList = giftCertificateDTO.getTags();
            if (tagDTOList != null && tagDTOList.size() != 0) {
                List<Tag> oldTagList = tagRepository.findListTagsByCertificateId(giftCertificate.getId());
                List<TagDTO> updateTagDTOList = tagDTOList.stream().filter(task -> task.getId() != null).collect(Collectors.toList());
                List<TagDTO> createTagList = tagDTOList.stream().filter(task -> task.getId() == null).collect(Collectors.toList());

                List<Tag> deleteTagList = new ArrayList<>(oldTagList);
                deleteTagList.removeAll(updateTagDTOList.stream().map(tagConverter::toEntity).collect(Collectors.toList()));

                List<TagDTO> addTagList = new ArrayList<>(tagDTOList);
                addTagList.removeAll(createTagList);
                addTagList.removeAll(oldTagList.stream().map(tagConverter::toDTO).collect(Collectors.toList()));

                for (TagDTO tagDTO : createTagList) {
                    Tag tag = tagRepository.findByName(tagDTO.getName());
                    if (tag == null) {
                        Tag newTag = new Tag();
                        newTag.setName(tagDTO.getName());
                        Long newTagId = tagRepository.create(newTag);
                        tagRepository.createConnectionBetweenTagAndGiftCertificate(newTagId, giftCertificate.getId());
                    } else {
                        tagRepository.createConnectionBetweenTagAndGiftCertificate(tag.getId(), giftCertificate.getId());
                    }
                }
                addTagList.forEach(tagDTO -> tagRepository.createConnectionBetweenTagAndGiftCertificate(tagDTO.getId(), giftCertificate.getId()));

                deleteTagList.forEach(tag -> tagRepository.deleteConnectionBetweenTagAndGiftCertificate(tag.getId(), giftCertificate.getId()));
            }
        }
        return result;
    }
}