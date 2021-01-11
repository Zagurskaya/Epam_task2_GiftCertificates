package com.epam.esm.impl;

import com.epam.esm.exception.DaoException;
import com.epam.esm.model.GiftCertificate;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.epam.esm.GiftCertificateRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Repository
class GiftCertificateRepositoryImpl implements GiftCertificateRepository {

    private static final Logger logger = LogManager.getLogger(GiftCertificateRepositoryImpl.class);

    private static final String SQL_SELECT_ALL_GIFT_CERTIFICATES = "SELECT id, name, description, price, duration, creationDate, lastUpdateDate FROM giftCertificate ";
    private static final String SQL_SELECT_ALL_GIFT_CERTIFICATES_BY_TAG_NAME =
            "SELECT giftcertificate.id as id, giftcertificate.name as name, giftcertificate.description as description, giftcertificate.price as price, giftcertificate.duration as duration, giftcertificate.creationDate as creationDate,giftcertificate.lastUpdateDate as lastUpdateDate\n" +
                    "FROM certificate_tag\n" +
                    "LEFT JOIN giftcertificate\n" +
                    "ON certificate_tag.certificateId = giftcertificate.id\n" +
                    "LEFT JOIN tag\n" +
                    "ON certificate_tag.tagId = tag.id\n" +
                    "WHERE tag.name = ? ";
    private static final String SQL_SELECT_GIFT_CERTIFICATE_BY_ID = "SELECT id, name, description, price, duration, creationDate, lastUpdateDate FROM giftCertificate WHERE id= ? ";
    private static final String SQL_SELECT_GIFT_CERTIFICATE_BY_NAME = "SELECT id, name, description, price, duration, creationDate, lastUpdateDate FROM giftCertificate WHERE name = ? ";
    private static final String SQL_INSERT_GIFT_CERTIFICATE = "INSERT INTO giftCertificate(name, description, price, duration, creationDate, lastUpdateDate) VALUES (?, ?, ?, ?, ?, ?)";
    private static final String SQL_UPDATE_GIFT_CERTIFICATE = "UPDATE giftCertificate SET name=?, description=?, price=?, duration=?, creationDate=?, lastUpdateDate=? WHERE id= ?";
    private static final String SQL_DELETE_GIFT_CERTIFICATE = "DELETE FROM giftCertificate WHERE id=?";

    @Override
    public GiftCertificate findById(Connection connection, Long id) throws DaoException {
        GiftCertificate giftCertificate = null;
        try {
            try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_SELECT_GIFT_CERTIFICATE_BY_ID)) {
                preparedStatement.setLong(1, id);
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    String name = resultSet.getString(ColumnName.GIFT_CERTIFICATE_NAME);
                    String description = resultSet.getString(ColumnName.GIFT_CERTIFICATE_DESCRIPTION);
                    BigDecimal price = resultSet.getBigDecimal(ColumnName.GIFT_CERTIFICATE_PRICE);
                    Integer duration = resultSet.getInt(ColumnName.GIFT_CERTIFICATE_DURATION);
                    LocalDateTime creationDate = resultSet.getObject(ColumnName.GIFT_CERTIFICATE_CREATION_DATE, LocalDateTime.class);
                    LocalDateTime lastUpdateDate = resultSet.getObject(ColumnName.GIFT_CERTIFICATE_LAST_UPDATE_DATE, LocalDateTime.class);

                    giftCertificate = new GiftCertificate();
                    giftCertificate.setId(id);
                    giftCertificate.setName(name);
                    giftCertificate.setDescription(description);
                    giftCertificate.setDuration(duration);
                    giftCertificate.setPrice(price);
                    giftCertificate.setCreationDate(creationDate);
                    giftCertificate.setLastUpdateDate(lastUpdateDate);
                }
            }
        } catch (SQLException e) {
            logger.log(Level.ERROR, "Database exception during fiend giftCertificate by id", e);
            throw new DaoException("Database exception during fiend giftCertificate by id", e);
        }
        return giftCertificate;
    }

    @Override
    public Long create(Connection connection, GiftCertificate giftCertificate) throws DaoException {
        int result;
        try {
            try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_INSERT_GIFT_CERTIFICATE, Statement.RETURN_GENERATED_KEYS)) {
                preparedStatement.setString(1, giftCertificate.getName());
                preparedStatement.setString(2, giftCertificate.getDescription());
                preparedStatement.setBigDecimal(3, giftCertificate.getPrice());
                preparedStatement.setInt(4, giftCertificate.getDuration());
                preparedStatement.setString(5, giftCertificate.getCreationDate().toString());
                preparedStatement.setString(6, giftCertificate.getLastUpdateDate().toString());
                result = preparedStatement.executeUpdate();
                if (1 == result) {
                    ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
                    if (generatedKeys.next()) {
                        return generatedKeys.getLong(1);
                    }
                }
            }
        } catch (SQLException e) {
            logger.log(Level.ERROR, "Database exception during create giftCertificate", e);
            throw new DaoException("Database exception during create giftCertificate", e);
        }
        return 0L;
    }

    @Override
    public boolean update(Connection connection, GiftCertificate giftCertificate) throws DaoException {
        int result;
        try {
            try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_UPDATE_GIFT_CERTIFICATE)) {
                preparedStatement.setString(1, giftCertificate.getName());
                preparedStatement.setString(2, giftCertificate.getDescription());
                preparedStatement.setBigDecimal(3, giftCertificate.getPrice());
                preparedStatement.setInt(4, giftCertificate.getDuration());
                preparedStatement.setString(5, giftCertificate.getCreationDate().toString());
                preparedStatement.setString(6, giftCertificate.getLastUpdateDate().toString());
                preparedStatement.setLong(7, giftCertificate.getId());
                result = preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            logger.log(Level.ERROR, "Database exception during update giftCertificate", e);
            throw new DaoException("Database exception during update giftCertificate ", e);
        }
        return 1 == result;
    }

    @Override
    public boolean delete(Connection connection, Long id) throws DaoException {
        int result;
        try {
            try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_DELETE_GIFT_CERTIFICATE)) {
                preparedStatement.setLong(1, id);
                result = preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            logger.log(Level.ERROR, "Database exception during delete giftCertificate", e);
            throw new DaoException("Database exception during delete giftCertificate ", e);
        }
        return 1 == result;
    }

    @Override
    public List<GiftCertificate> findAll(Connection connection) throws DaoException {
        List<GiftCertificate> giftCertificates = new ArrayList<>();
        try {
            try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_SELECT_ALL_GIFT_CERTIFICATES)) {
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    Long id = resultSet.getLong(ColumnName.GIFT_CERTIFICATE_ID);
                    String name = resultSet.getString(ColumnName.GIFT_CERTIFICATE_NAME);
                    String description = resultSet.getString(ColumnName.GIFT_CERTIFICATE_DESCRIPTION);
                    BigDecimal price = resultSet.getBigDecimal(ColumnName.GIFT_CERTIFICATE_PRICE);
                    Integer duration = resultSet.getInt(ColumnName.GIFT_CERTIFICATE_DURATION);
                    LocalDateTime creationDate = resultSet.getObject(ColumnName.GIFT_CERTIFICATE_CREATION_DATE, LocalDateTime.class);
                    LocalDateTime lastUpdateDate = resultSet.getObject(ColumnName.GIFT_CERTIFICATE_LAST_UPDATE_DATE, LocalDateTime.class);

                    GiftCertificate giftCertificate = new GiftCertificate();
                    giftCertificate.setId(id);
                    giftCertificate.setName(name);
                    giftCertificate.setDescription(description);
                    giftCertificate.setDuration(duration);
                    giftCertificate.setPrice(price);
                    giftCertificate.setCreationDate(creationDate);
                    giftCertificate.setLastUpdateDate(lastUpdateDate);
                    giftCertificates.add(giftCertificate);
                }
            }
        } catch (SQLException e) {
            logger.log(Level.ERROR, "Database exception during fiend all giftCertificate", e);
            throw new DaoException("Database exception during fiend all giftCertificate", e);
        }
        return giftCertificates;
    }

    @Override
    public GiftCertificate findByName(Connection connection, String name) throws DaoException {
        GiftCertificate giftCertificate = null;
        try {
            try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_SELECT_GIFT_CERTIFICATE_BY_NAME)) {
                preparedStatement.setString(1, name);
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    Long id = resultSet.getLong(ColumnName.GIFT_CERTIFICATE_ID);
                    String description = resultSet.getString(ColumnName.GIFT_CERTIFICATE_DESCRIPTION);
                    BigDecimal price = resultSet.getBigDecimal(ColumnName.GIFT_CERTIFICATE_PRICE);
                    Integer duration = resultSet.getInt(ColumnName.GIFT_CERTIFICATE_DURATION);
                    LocalDateTime creationDate = resultSet.getObject(ColumnName.GIFT_CERTIFICATE_CREATION_DATE, LocalDateTime.class);
                    LocalDateTime lastUpdateDate = resultSet.getObject(ColumnName.GIFT_CERTIFICATE_LAST_UPDATE_DATE, LocalDateTime.class);

                    giftCertificate = new GiftCertificate();
                    giftCertificate.setId(id);
                    giftCertificate.setName(name);
                    giftCertificate.setDescription(description);
                    giftCertificate.setDuration(duration);
                    giftCertificate.setCreationDate(creationDate);
                    giftCertificate.setLastUpdateDate(lastUpdateDate);
                }
            }
        } catch (SQLException e) {
            logger.log(Level.ERROR, "Database exception during fiend giftCertificate by name", e);
            throw new DaoException("Database exception during fiend giftCertificate by name", e);
        }
        return giftCertificate;
    }

    @Override
    public List<GiftCertificate> findAllByTagName(Connection connection, String tagName) {
        List<GiftCertificate> giftCertificates = new ArrayList<>();
        try {
            try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_SELECT_ALL_GIFT_CERTIFICATES_BY_TAG_NAME)) {
                preparedStatement.setString(1, tagName);
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    Long id = resultSet.getLong(ColumnName.GIFT_CERTIFICATE_ID);
                    String name = resultSet.getString(ColumnName.GIFT_CERTIFICATE_NAME);
                    String description = resultSet.getString(ColumnName.GIFT_CERTIFICATE_DESCRIPTION);
                    BigDecimal price = resultSet.getBigDecimal(ColumnName.GIFT_CERTIFICATE_PRICE);
                    Integer duration = resultSet.getInt(ColumnName.GIFT_CERTIFICATE_DURATION);
                    LocalDateTime creationDate = resultSet.getObject(ColumnName.GIFT_CERTIFICATE_CREATION_DATE, LocalDateTime.class);
                    LocalDateTime lastUpdateDate = resultSet.getObject(ColumnName.GIFT_CERTIFICATE_LAST_UPDATE_DATE, LocalDateTime.class);

                    GiftCertificate giftCertificate = new GiftCertificate();
                    giftCertificate.setId(id);
                    giftCertificate.setName(name);
                    giftCertificate.setDescription(description);
                    giftCertificate.setDuration(duration);
                    giftCertificate.setPrice(price);
                    giftCertificate.setCreationDate(creationDate);
                    giftCertificate.setLastUpdateDate(lastUpdateDate);
                    giftCertificates.add(giftCertificate);
                }
            }
        } catch (SQLException e) {
            logger.log(Level.ERROR, "Database exception during fiend all giftCertificate", e);
            throw new DaoException("Database exception during fiend all giftCertificate", e);
        }
        return giftCertificates;    }
}