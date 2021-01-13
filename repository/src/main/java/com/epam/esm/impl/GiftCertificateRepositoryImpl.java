package com.epam.esm.impl;

import com.epam.esm.GiftCertificateRepository;
import com.epam.esm.model.GiftCertificate;

import com.epam.esm.rowmapper.GiftCertificateRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.util.List;

@Repository
class GiftCertificateRepositoryImpl implements GiftCertificateRepository {

    JdbcTemplate jdbcTemplate;

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
    private static final String SQL_DELETE_GIFT_CERTIFICATE = "DELETE FROM giftCertificate WHERE id=?";

    @Autowired
    public GiftCertificateRepositoryImpl(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public List<GiftCertificate> findAll() {
        List<GiftCertificate> result = jdbcTemplate.query(SQL_SELECT_ALL_GIFT_CERTIFICATES, new GiftCertificateRowMapper());
        return result;
    }

    @Override
    public GiftCertificate findById(Long id) {
        List<GiftCertificate> result = jdbcTemplate.query(SQL_SELECT_GIFT_CERTIFICATE_BY_ID, new GiftCertificateRowMapper(), id);
        return result.isEmpty() ? null : result.get(0);
    }

    @Override
    public Long create(GiftCertificate giftCertificate) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
                connection -> {
                    PreparedStatement ps = connection.prepareStatement(SQL_INSERT_GIFT_CERTIFICATE, new String[]{"id"});
                    ps.setString(1, giftCertificate.getName());
                    ps.setString(2, giftCertificate.getDescription());
                    ps.setBigDecimal(3, giftCertificate.getPrice());
                    ps.setInt(4, giftCertificate.getDuration());
                    ps.setString(5, giftCertificate.getCreationDate().toString());
                    ps.setString(6, giftCertificate.getLastUpdateDate().toString());
                    return ps;
                },
                keyHolder);
        return keyHolder.getKey().longValue();
    }

    @Override
    public boolean update(GiftCertificate giftCertificate) {

        StringBuilder SQL_UPDATE_GIFT_CERTIFICATE = new StringBuilder();
        SQL_UPDATE_GIFT_CERTIFICATE.append("UPDATE giftCertificate SET ");
        if (giftCertificate.getName() != null) {
            SQL_UPDATE_GIFT_CERTIFICATE.append(" name=?,");
        }
        if (giftCertificate.getDescription() != null) {
            SQL_UPDATE_GIFT_CERTIFICATE.append(" description=?,");
        }
        if (giftCertificate.getPrice() != null) {
            SQL_UPDATE_GIFT_CERTIFICATE.append(" price=?,");
        }
        if (giftCertificate.getDuration() != null) {
            SQL_UPDATE_GIFT_CERTIFICATE.append(" duration=?,");
        }
        if (giftCertificate.getCreationDate() != null) {
            SQL_UPDATE_GIFT_CERTIFICATE.append(" creationDate=?,");
        }
        if (giftCertificate.getLastUpdateDate() != null) {
            SQL_UPDATE_GIFT_CERTIFICATE.append(" lastUpdateDate=?");
        }
        SQL_UPDATE_GIFT_CERTIFICATE.append(" WHERE id= ?");

        int result = jdbcTemplate.update(
                connection -> {
                    PreparedStatement ps = connection.prepareStatement(SQL_UPDATE_GIFT_CERTIFICATE.toString());
                    int i = 1;
                    if (giftCertificate.getName() != null) {
                        ps.setString(i, giftCertificate.getName());
                        i++;
                    }
                    if (giftCertificate.getDescription() != null) {
                        ps.setString(i, giftCertificate.getDescription());
                        i++;
                    }
                    if (giftCertificate.getPrice() != null) {
                        ps.setBigDecimal(i, giftCertificate.getPrice());
                        i++;
                    }
                    if (giftCertificate.getDuration() != null) {
                        ps.setInt(i, giftCertificate.getDuration());
                        i++;
                    }
                    if (giftCertificate.getCreationDate() != null) {
                        ps.setString(i, giftCertificate.getCreationDate().toString());
                        i++;
                    }
                    ps.setString(i, giftCertificate.getLastUpdateDate().toString());
                    ps.setLong(i + 1, giftCertificate.getId());
                    return ps;
                });

        return 1 == result;
    }

    @Override
    public boolean delete(Long id) {
        return 1 == jdbcTemplate.update(SQL_DELETE_GIFT_CERTIFICATE, id);
    }

    @Override
    public GiftCertificate findByName(String name) {
        List<GiftCertificate> certificates = jdbcTemplate.query(SQL_SELECT_GIFT_CERTIFICATE_BY_NAME, new GiftCertificateRowMapper(), name);
        return certificates.isEmpty() ? null : certificates.get(0);
    }

    @Override
    public List<GiftCertificate> findAllByTagName(String tagName) {
        return jdbcTemplate.query(SQL_SELECT_ALL_GIFT_CERTIFICATES_BY_TAG_NAME, new GiftCertificateRowMapper());
    }
}