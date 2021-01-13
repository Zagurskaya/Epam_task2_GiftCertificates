package com.epam.esm.impl;

import com.epam.esm.GiftCertificateRepository;
import com.epam.esm.GiftCertificateService;
import com.epam.esm.TagRepository;
import com.epam.esm.connection.ConnectionHandler;
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
    private final ConnectionHandler connectionHandler;

    @Autowired
    public GiftCertificateServiceImpl(
            GiftCertificateRepository giftCertificateRepository,
            GiftCertificateConverter giftCertificateConverter,
            TagRepository tagRepository,
            TagConverter tagConverter,
            ConnectionHandler connectionHandler
    ) {
        this.giftCertificateRepository = giftCertificateRepository;
        this.giftCertificateConverter = giftCertificateConverter;
        this.tagRepository = tagRepository;
        this.tagConverter = tagConverter;
        this.connectionHandler = connectionHandler;
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
            if (!giftCertificateDTO.getName().equals(giftCertificate.getName())) {
                giftCertificate.setName(giftCertificateDTO.getName());
            }
            if (!giftCertificateDTO.getDescription().equals(giftCertificate.getDescription())) {
                giftCertificate.setDescription(giftCertificateDTO.getDescription());
            }
            if (!giftCertificateDTO.getPrice().equals(giftCertificate.getPrice())) {
                giftCertificate.setPrice(giftCertificateDTO.getPrice());
            }
            if (!giftCertificateDTO.getDuration().equals(giftCertificate.getDuration())) {
                giftCertificate.setDuration(giftCertificateDTO.getDuration());
            }
            updateGiftCertificate.setLastUpdateDate(LocalDateTime.now());
            result = giftCertificateRepository.update(updateGiftCertificate);

            List<TagDTO> tagDTOList = giftCertificateDTO.getTags();
            if (tagDTOList.size() != 0) {
                List<Long> tagIdList = tagDTOList.stream().map(TagDTO::getId).collect(Collectors.toList());
                List<Tag> oldTagList = tagRepository.findListTagsByCertificateId(giftCertificate.getId());
                List<Long> oldTagIdList = oldTagList.stream().map(Tag::getId).collect(Collectors.toList());

                List<TagDTO> updateTagDTOList = tagDTOList.stream().filter(task -> task.getId() != null).collect(Collectors.toList());
                List<Long> idListUpdateTagDTOList = updateTagDTOList.stream().map(TagDTO::getId).collect(Collectors.toList());

                List<TagDTO> createTagList = tagDTOList.stream().filter(task -> task.getId() == null).collect(Collectors.toList());

                if (!tagIdList.containsAll(idListUpdateTagDTOList)) {
                    throw new ServiceException("Tag List isn't relevant");
                }

                List<Long> deleteTagIdList = new ArrayList<>(oldTagIdList);
                deleteTagIdList.removeAll(idListUpdateTagDTOList);

                List<TagDTO> addTagList = new ArrayList<>(tagDTOList);
                addTagList.removeAll(createTagList);
                addTagList.removeAll(oldTagList.stream().map(tagConverter::toDTO).collect(Collectors.toList()));

                for (TagDTO tagDTO : createTagList) {
                    Tag newTag = new Tag();
                    newTag.setName(tagDTO.getName());
                    Long newTagId = tagRepository.create(newTag);
                    tagRepository.createConnectionBetweenTagAndGiftCertificate(newTagId, giftCertificate.getId());
                }
                addTagList.forEach(tagDTO -> tagRepository.createConnectionBetweenTagAndGiftCertificate(tagDTO.getId(), giftCertificate.getId()));

                deleteTagIdList.forEach(id -> tagRepository.deleteConnectionBetweenTagAndGiftCertificate(id, giftCertificate.getId()));
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
        if (tagList.size() != 0) {
            tagList.forEach(tag -> tagRepository.deleteConnectionBetweenTagAndGiftCertificate(tag.getId(), id));
        }
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
}