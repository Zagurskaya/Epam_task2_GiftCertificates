package com.epam.esm.impl;

import com.epam.esm.TagRepository;
import com.epam.esm.TagService;
import com.epam.esm.connection.ConnectionHandler;
import com.epam.esm.converter.TagConverter;
import com.epam.esm.exception.ServiceException;
import com.epam.esm.model.Tag;
import com.epam.esm.model.TagDTO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TagServiceImpl implements TagService {

    private static final String TAG_ADDING_ERROR_MESSAGE = "Error while adding Tag.";
    private static final String TAGS_GETTING_ERROR_MESSAGE = "Error while getting Tags list.";
    private static final String TAG_UPDATING_ERROR_MESSAGE = "Error while updating Tag status.";
    private static final String CONNECTION_CLOSE_ERROR_MESSAGE = "Error while closing connection.";

    private static final Logger logger = LogManager.getLogger(TagRepositoryImpl.class);

    private final TagRepository tagRepository;
    private final TagConverter tagConverter;
    private final ConnectionHandler connectionHandler;

    @Autowired
    public TagServiceImpl(
            TagRepository tagRepository,
            TagConverter tagConverter,
            ConnectionHandler connectionHandler
    ) {
        this.tagRepository = tagRepository;
        this.tagConverter = tagConverter;
        this.connectionHandler = connectionHandler;
    }

    @Override
    public TagDTO findById(Long id) throws ServiceException {
        try (Connection connection = connectionHandler.getConnection()) {
            try {
                connection.setAutoCommit(false);
                Tag tag = tagRepository.findById(connection, id);
                connection.commit();
                return tagConverter.toDTO(tag);
            } catch (SQLException e) {
                connection.rollback();
                logger.error(e.getMessage(), e);
                throw new ServiceException(TAG_ADDING_ERROR_MESSAGE, e);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new ServiceException(CONNECTION_CLOSE_ERROR_MESSAGE, e);
        }
    }

    @Override
    public Long create(TagDTO tagDTO) {
        try (Connection connection = connectionHandler.getConnection()) {
            try {
                connection.setAutoCommit(false);
                Tag tag = tagConverter.toEntity(tagDTO);
                Long id = tagRepository.create(connection, tag);
                connection.commit();
                return id;
            } catch (SQLException e) {
                connection.rollback();
                logger.error(e.getMessage(), e);
                throw new ServiceException(TAG_ADDING_ERROR_MESSAGE, e);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new ServiceException(CONNECTION_CLOSE_ERROR_MESSAGE, e);
        }
    }

    @Override
    public List<TagDTO> findAll() {
        try (Connection connection = connectionHandler.getConnection()) {
            try {
                connection.setAutoCommit(false);
                List<Tag> tags = tagRepository.findAll(connection);
                List<TagDTO> tagDTOs = tags.stream().map(tagConverter::toDTO).collect(Collectors.toList());
                connection.commit();
                return tagDTOs;
            } catch (SQLException e) {
                connection.rollback();
                logger.error(e.getMessage(), e);
                throw new ServiceException(TAGS_GETTING_ERROR_MESSAGE, e);
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
                Tag tag = tagRepository.findById(connection, id);
                if (tag == null) {
                    connection.commit();
                    return result;
                }
                result = tagRepository.delete(connection, id);
                connection.commit();
                return result;
            } catch (SQLException e) {
                connection.rollback();
                logger.error(e.getMessage(), e);
                throw new ServiceException(TAG_UPDATING_ERROR_MESSAGE, e);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new ServiceException(CONNECTION_CLOSE_ERROR_MESSAGE, e);
        }
    }
}