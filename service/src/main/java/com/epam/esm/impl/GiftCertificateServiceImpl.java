package com.epam.esm.impl;

import com.epam.esm.GiftCertificateRepository;
import com.epam.esm.GiftCertificateService;
import com.epam.esm.connection.ConnectionHandler;
import com.epam.esm.converter.GiftCertificateConverter;
import com.epam.esm.exception.ServiceException;
import com.epam.esm.model.GiftCertificate;
import com.epam.esm.model.GiftCertificateDTO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Service
public class GiftCertificateServiceImpl implements GiftCertificateService {

    private static final String GIFT_CERTIFICATE_ADDING_ERROR_MESSAGE = "Error while adding GiftCertificate.";
    private static final String GIFT_CERTIFICATES_GETTING_ERROR_MESSAGE = "Error while getting GiftCertificates list.";
    private static final String GIFT_CERTIFICATE_UPDATING_ERROR_MESSAGE = "Error while updating GiftCertificate status.";
    private static final String CONNECTION_CLOSE_ERROR_MESSAGE = "Error while closing connection.";

    private static final Logger logger = LogManager.getLogger(GiftCertificateRepositoryImpl.class);

    private GiftCertificateRepository giftCertificateRepository;
    private GiftCertificateConverter giftCertificateConverter;
    private ConnectionHandler connectionHandler;

    @Autowired
    public GiftCertificateServiceImpl(
            GiftCertificateRepository giftCertificateRepository,
            GiftCertificateConverter giftCertificateConverter,
            ConnectionHandler connectionHandler
    ) {
        this.giftCertificateRepository = giftCertificateRepository;
        this.giftCertificateConverter = giftCertificateConverter;
        this.connectionHandler = connectionHandler;
    }

    @Override
    public GiftCertificateDTO findById(Long id) throws ServiceException {
        try (Connection connection = connectionHandler.getConnection()) {
            try {
                connection.setAutoCommit(false);
                GiftCertificate giftCertificate = giftCertificateRepository.findById(connection, id);
                connection.commit();
                return giftCertificateConverter.toDTO(giftCertificate);
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
                List<GiftCertificateDTO> giftCertificateDTOs = getGiftCertificateDTOs(giftCertificates);
                connection.commit();
                return giftCertificateDTOs;
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
                giftCertificate.setName(giftCertificateDTO.getName());
                result = giftCertificateRepository.update(connection, giftCertificate);
                connection.commit();
                return result;
            } catch (SQLException e) {
                connection.rollback();
                logger.error(e.getMessage(), e);
                throw new ServiceException(GIFT_CERTIFICATE_UPDATING_ERROR_MESSAGE, e);
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
                result = giftCertificateRepository.delete(connection, id);
                connection.commit();
                return result;
            } catch (SQLException e) {
                connection.rollback();
                logger.error(e.getMessage(), e);
                throw new ServiceException(GIFT_CERTIFICATE_UPDATING_ERROR_MESSAGE, e);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new ServiceException(CONNECTION_CLOSE_ERROR_MESSAGE, e);
        }    }

    private List<GiftCertificateDTO> getGiftCertificateDTOs(List<GiftCertificate> giftCertificates) {
        List<GiftCertificateDTO> giftCertificateDTOs = new ArrayList<>();
        for (GiftCertificate giftCertificate : giftCertificates) {
            giftCertificateDTOs.add(giftCertificateConverter.toDTO(giftCertificate));
        }
        return giftCertificateDTOs;
    }
}