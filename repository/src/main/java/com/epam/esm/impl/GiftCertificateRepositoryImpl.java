package com.epam.esm.impl;

import com.epam.esm.GiftCertificateRepository;
import com.epam.esm.model.GiftCertificate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Repository
class GiftCertificateRepositoryImpl implements GiftCertificateRepository {

    private static final String SQL_SELECT_ALL_GIFT_CERTIFICATES = "SELECT id, name, description, price, duration, creationDate, lastUpdateDate FROM giftCertificate ";
    private static final String SQL_SELECT_ALL_GIFT_CERTIFICATES_BY_FILTER = "SELECT giftcertificate.id, giftcertificate.name, giftcertificate.description,giftcertificate.price, giftcertificate.duration, giftcertificate.creationDate, giftcertificate.lastUpdateDate \n" +
            "FROM giftcertificate\n" +
            "LEFT JOIN certificate_tag\n" +
            "ON giftcertificate.id = certificate_tag.certificateId\n" +
            "LEFT JOIN tag \n" +
            "ON certificate_tag.tagId = tag.id\n" +
            "WHERE  \n";
    private static final String SQL_SELECT_GIFT_CERTIFICATE_BY_ID = "SELECT id, name, description, price, duration, creationDate, lastUpdateDate FROM giftCertificate WHERE id= ? ";
    private static final String SQL_INSERT_GIFT_CERTIFICATE = "INSERT INTO giftCertificate(name, description, price, duration, creationDate, lastUpdateDate) VALUES (?, ?, ?, ?, ?, ?)";
    private static final String SQL_DELETE_GIFT_CERTIFICATE = "DELETE FROM giftCertificate WHERE id=?";
    private static final String SQL_UPDATE_GIFT_CERTIFICATE = "UPDATE giftCertificate SET name=?, description=?, price=?, duration=?, creationDate=?, lastUpdateDate=? WHERE id= ?";

    private JdbcTemplate jdbcTemplate;

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
        GiftCertificate certificate = jdbcTemplate.queryForObject(SQL_SELECT_GIFT_CERTIFICATE_BY_ID, new Object[]{id}, new GiftCertificateRowMapper());
        return certificate;
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
                    ps.setLong(4, giftCertificate.getDuration());
                    ps.setString(5, giftCertificate.getCreationDate().toString());
                    ps.setString(6, giftCertificate.getLastUpdateDate().toString());
                    return ps;
                },
                keyHolder);
        return keyHolder.getKey().longValue();
    }

    @Override
    public boolean updatePart(GiftCertificate giftCertificate) {

        String sqlUpdatePart = "UPDATE giftCertificate SET ";
        if (giftCertificate.getName() != null) {
            sqlUpdatePart = sqlUpdatePart + " name=?,";
        }
        if (giftCertificate.getDescription() != null) {
            sqlUpdatePart = sqlUpdatePart + " description=?,";
        }
        if (giftCertificate.getPrice() != null) {
            sqlUpdatePart = sqlUpdatePart + " price=?,";
        }
        if (giftCertificate.getDuration() != null) {
            sqlUpdatePart = sqlUpdatePart + " duration=?,";
        }
        if (giftCertificate.getCreationDate() != null) {
            sqlUpdatePart = sqlUpdatePart + " creationDate=?,";
        }
        if (giftCertificate.getLastUpdateDate() != null) {
            sqlUpdatePart = sqlUpdatePart + " lastUpdateDate=?";
        }
        sqlUpdatePart = sqlUpdatePart + " WHERE id= ?";

        String finalSqlUpdatePart = sqlUpdatePart;
        int result = jdbcTemplate.update(
                connection -> {
                    PreparedStatement ps = connection.prepareStatement(finalSqlUpdatePart.toString());
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
                        ps.setLong(i, giftCertificate.getDuration());
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
    public boolean update(GiftCertificate giftCertificate) {
        return jdbcTemplate.update(SQL_UPDATE_GIFT_CERTIFICATE,
                giftCertificate.getName(), giftCertificate.getDescription(), giftCertificate.getPrice(), giftCertificate.getDuration(),
                giftCertificate.getCreationDate().toString(), giftCertificate.getLastUpdateDate().toString(), giftCertificate.getId()) > 0;

    }

    @Override
    public boolean delete(Long id) {
        return 1 == jdbcTemplate.update(SQL_DELETE_GIFT_CERTIFICATE, id);
    }

    @Override
    public List<GiftCertificate> findAllByFilter(Map<String, String> filter) {

        String sqlFilter = SQL_SELECT_ALL_GIFT_CERTIFICATES_BY_FILTER;
        boolean isAddAnd = false;

        if (filter.get("tagName") != null) {
            sqlFilter = sqlFilter + " tag.name =? ";
            isAddAnd = true;
        }
        if (filter.get("partName") != null) {
            if (isAddAnd) {
                sqlFilter = sqlFilter + " AND ";
            }
            sqlFilter = sqlFilter + " giftcertificate.name LIKE ? ";
            isAddAnd = true;
        }
        if (filter.get("partDescription") != null) {
            if (isAddAnd) {
                sqlFilter = sqlFilter + " AND ";
            }
            sqlFilter = sqlFilter + " giftcertificate.description LIKE ?";
        }
        if (filter.get("orderBy") != null) {
            if (filter.get("orderBy").equals("name")) {
                sqlFilter = sqlFilter + "ORDER BY giftcertificate.name ";
            }
            if (filter.get("orderBy").equals("date")) {
                sqlFilter = sqlFilter + "ORDER BY giftcertificate.creationDate ";
            }
            if (filter.get("orderValue") != null) {
                if (filter.get("orderValue").toUpperCase().equals("DESC")) {
                    sqlFilter = sqlFilter + "DESC";
                }
            }
            sqlFilter = sqlFilter.replaceAll("WHERE  \nORDER BY", "ORDER BY");
        }

        String finalSqlFilter = sqlFilter;
        List<GiftCertificate> certificates = jdbcTemplate.query(
                connection -> {
                    PreparedStatement ps = connection.prepareStatement(finalSqlFilter);
                    int i = 1;
                    if (filter.get("tagName") != null) {
                        ps.setString(i, filter.get("tagName"));
                        i++;
                    }
                    if (filter.get("partName") != null) {
                        ps.setString(i, "%" + filter.get("partName") + "%");
                        i++;
                    }
                    if (filter.get("partDescription") != null) {
                        ps.setString(i, "%" + filter.get("partDescription") + "%");
                        i++;
                    }
                    return ps;
                }, new GiftCertificateRowMapper());
        return certificates;
    }

    class GiftCertificateRowMapper implements RowMapper<GiftCertificate> {

        @Override
        public GiftCertificate mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new GiftCertificate(
                    rs.getLong(ColumnName.GIFT_CERTIFICATE_ID),
                    rs.getString(ColumnName.GIFT_CERTIFICATE_NAME),
                    rs.getString(ColumnName.GIFT_CERTIFICATE_DESCRIPTION),
                    rs.getBigDecimal(ColumnName.GIFT_CERTIFICATE_PRICE),
                    rs.getLong(ColumnName.GIFT_CERTIFICATE_DURATION),
                    rs.getObject(ColumnName.GIFT_CERTIFICATE_CREATION_DATE, LocalDateTime.class),
                    rs.getObject(ColumnName.GIFT_CERTIFICATE_LAST_UPDATE_DATE, LocalDateTime.class)
            );
        }
    }
}