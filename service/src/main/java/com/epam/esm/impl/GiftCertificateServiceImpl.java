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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GiftCertificateServiceImpl implements GiftCertificateService {

    private static final String GIFT_CERTIFICATE_ADDING_ERROR_MESSAGE = "Error while adding GiftCertificate.";
    private static final String GIFT_CERTIFICATES_GETTING_ERROR_MESSAGE = "Error while getting GiftCertificates list.";
    private static final String GIFT_CERTIFICATE_UPDATING_ERROR_MESSAGE = "Error while updating GiftCertificate.";
    private static final String GIFT_CERTIFICATE_TAGS_UPDATING_ERROR_MESSAGE = "Error while updating GiftCertificate and tag list.";
    private static final String GIFT_CERTIFICATE_DELETING_ERROR_MESSAGE = "Error while deleting GiftCertificate.";
    private static final String CONNECTION_CLOSE_ERROR_MESSAGE = "Error while closing connection.";

    private static final Logger logger = LogManager.getLogger(GiftCertificateRepositoryImpl.class);

    private GiftCertificateRepository giftCertificateRepository;
    private GiftCertificateConverter giftCertificateConverter;
    private TagRepository tagRepository;
    private TagConverter tagConverter;
    private ConnectionHandler connectionHandler;

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
    public GiftCertificateDTO findById(Long id) throws ServiceException {
        GiftCertificateDTO giftCertificateDTO = null;
        try (Connection connection = connectionHandler.getConnection()) {
            try {
                connection.setAutoCommit(false);
                GiftCertificate giftCertificate = giftCertificateRepository.findById(connection, id);
                List<Tag> tags = tagRepository.findListTagsByCertificateId(connection, id);

                giftCertificateDTO = giftCertificateConverter.toDTO(giftCertificate);
                List<TagDTO> tagDTOList = tags.stream().map(tagConverter::toDTO).collect(Collectors.toList());
                giftCertificateDTO.setTags(tagDTOList);
                connection.commit();
                return giftCertificateDTO;
            } catch (SQLException e) {
                connection.rollback();
                logger.error(e.getMessage(), e);
                throw new ServiceException(GIFT_CERTIFICATE_ADDING_ERROR_MESSAGE, e);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new ServiceException(CONNECTION_CLOSE_ERROR_MESSAGE, e);
        }
    }

    @Override
    public Long create(GiftCertificateDTO giftCertificateDTO) {
        try (Connection connection = connectionHandler.getConnection()) {
            try {
                connection.setAutoCommit(false);
                GiftCertificate giftCertificate = giftCertificateConverter.toEntity(giftCertificateDTO);
                Long id = giftCertificateRepository.create(connection, giftCertificate);
                List<TagDTO> tagDTOList = giftCertificateDTO.getTags();
                if (tagDTOList != null && tagDTOList.size() != 0) {
                    tagDTOList.forEach(tagDTO -> {
                        Tag tagByName = tagRepository.findByName(connection, tagDTO.getName());
                        if (tagByName == null) {
                            Long tagId = tagRepository.create(connection, tagConverter.toEntity(tagDTO));
                            tagRepository.createConnectionBetweenTagAndGiftCertificate(connection, tagId, id);
                        } else {
                            tagRepository.createConnectionBetweenTagAndGiftCertificate(connection, tagByName.getId(), id);
                        }
                    });
                }
                connection.commit();
                return id;
            } catch (SQLException e) {
                connection.rollback();
                logger.error(e.getMessage(), e);
                throw new ServiceException(GIFT_CERTIFICATE_ADDING_ERROR_MESSAGE, e);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new ServiceException(CONNECTION_CLOSE_ERROR_MESSAGE, e);
        }
    }

    @Override
    public List<GiftCertificateDTO> findAll() {
        try (Connection connection = connectionHandler.getConnection()) {
            try {
                connection.setAutoCommit(false);
                List<GiftCertificate> giftCertificates = giftCertificateRepository.findAll(connection);
                List<GiftCertificateDTO> giftCertificateDTOS = giftCertificates.stream()
                        .map(giftCertificate -> giftCertificateConverter.toDTO(giftCertificate)).collect(Collectors.toList());
                giftCertificateDTOS.forEach(giftCertificateDTO -> {
                    List<Tag> tags = tagRepository.findListTagsByCertificateId(connection, giftCertificateDTO.getId());
                    List<TagDTO> tagDTOList = tags.stream().map(tagConverter::toDTO).collect(Collectors.toList());
                    giftCertificateDTO.setTags(tagDTOList);
                });
                connection.commit();
                return giftCertificateDTOS;
            } catch (SQLException e) {
                connection.rollback();
                logger.error(e.getMessage(), e);
                throw new ServiceException(GIFT_CERTIFICATES_GETTING_ERROR_MESSAGE, e);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new ServiceException(CONNECTION_CLOSE_ERROR_MESSAGE, e);
        }
    }

    @Override
    public GiftCertificateDTO findByName(String name) throws ServiceException {
        GiftCertificateDTO giftCertificateDTO = null;
        try (Connection connection = connectionHandler.getConnection()) {
            try {
                connection.setAutoCommit(false);
                GiftCertificate giftCertificate = giftCertificateRepository.findByName(connection, name);
                if (giftCertificate != null) {
                    List<Tag> tags = tagRepository.findListTagsByCertificateId(connection, giftCertificate.getId());
                    giftCertificateDTO = giftCertificateConverter.toDTO(giftCertificate);
                    List<TagDTO> tagDTOList = tags.stream().map(tagConverter::toDTO).collect(Collectors.toList());
                    giftCertificateDTO.setTags(tagDTOList);
                }
                connection.commit();
                return giftCertificateDTO;
            } catch (SQLException e) {
                connection.rollback();
                logger.error(e.getMessage(), e);
                throw new ServiceException(GIFT_CERTIFICATES_GETTING_ERROR_MESSAGE, e);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new ServiceException(CONNECTION_CLOSE_ERROR_MESSAGE, e);
        }
    }

    @Override
    public boolean update(GiftCertificateDTO giftCertificateDTO) {
        boolean result = false;
        try (Connection connection = connectionHandler.getConnection()) {
            try {
                connection.setAutoCommit(false);
                GiftCertificate giftCertificate = giftCertificateRepository.findById(connection, giftCertificateDTO.getId());
                if (giftCertificate == null) {
                    connection.commit();
                    return result;
                }
                if (giftCertificateDTO.getName() != null) {
                    giftCertificate.setName(giftCertificateDTO.getName());
                }
                if (giftCertificateDTO.getDescription() != null) {
                    giftCertificate.setDescription(giftCertificateDTO.getDescription());
                }
                if (giftCertificateDTO.getPrice() != null) {
                    giftCertificate.setPrice(giftCertificateDTO.getPrice());
                }
                if (giftCertificateDTO.getDuration() != null) {
                    giftCertificate.setDuration(giftCertificateDTO.getDuration());
                }
                giftCertificate.setLastUpdateDate(LocalDateTime.now());
                result = giftCertificateRepository.update(connection, giftCertificate);

                List<TagDTO> tagDTOList = giftCertificateDTO.getTags();
                if (tagDTOList != null && tagDTOList.size() != 0) {
                    List<Long> tagIdList = tagDTOList.stream().map(task -> task.getId()).collect(Collectors.toList());
                    List<Tag> oldTagList = tagRepository.findListTagsByCertificateId(connection, giftCertificate.getId());
                    List<Long> oldTagIdList = oldTagList.stream().map(task -> task.getId()).collect(Collectors.toList());

                    List<TagDTO> updateTagDTOList = tagDTOList.stream().filter(task -> task.getId() != null).collect(Collectors.toList());
                    List<Long> idListUpdateTagDTOList = updateTagDTOList.stream().map(task -> task.getId()).collect(Collectors.toList());

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
                        Long newTagId = tagRepository.create(connection, newTag);
                        tagRepository.createConnectionBetweenTagAndGiftCertificate(connection, newTagId, giftCertificate.getId());
                    }
                    addTagList.forEach(tagDTO -> {
                        tagRepository.createConnectionBetweenTagAndGiftCertificate(connection, tagDTO.getId(), giftCertificate.getId());
                    });

                    deleteTagIdList.forEach(id -> {
                        tagRepository.deleteConnectionBetweenTagAndGiftCertificate(connection, id, giftCertificate.getId());
                    });
                }
                connection.commit();
                return result;
            } catch (SQLException e) {
                connection.rollback();
                logger.error(e.getMessage(), e);
                throw new ServiceException(GIFT_CERTIFICATE_TAGS_UPDATING_ERROR_MESSAGE, e);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new ServiceException(CONNECTION_CLOSE_ERROR_MESSAGE, e);
        }
    }

    @Override
    public boolean delete(Long id) throws ServiceException {
        boolean result = false;
        try (Connection connection = connectionHandler.getConnection()) {
            try {
                connection.setAutoCommit(false);
                GiftCertificate giftCertificate = giftCertificateRepository.findById(connection, id);
                if (giftCertificate == null) {
                    connection.commit();
                    return result;
                }
                List<Tag> tagList = tagRepository.findListTagsByCertificateId(connection, giftCertificate.getId());
                if (tagList != null && tagList.size() != 0) {
                    tagList.forEach(tag -> tagRepository.deleteConnectionBetweenTagAndGiftCertificate(connection, tag.getId(), id));
                }
                result = giftCertificateRepository.delete(connection, id);
                connection.commit();
                return result;
            } catch (SQLException e) {
                connection.rollback();
                logger.error(e.getMessage(), e);
                throw new ServiceException(GIFT_CERTIFICATE_DELETING_ERROR_MESSAGE, e);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new ServiceException(CONNECTION_CLOSE_ERROR_MESSAGE, e);
        }
    }
}