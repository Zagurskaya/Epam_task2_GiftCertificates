package com.epam.esm.impl;

import com.epam.esm.RelationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.util.List;

@Repository
public class RelationRepositoryImpl implements RelationRepository {
    private static final String SQL_INSERT_CONNECTION_CERTIFICATE_TAG = "INSERT INTO certificate_tag(certificateId, tagId) VALUES (?, ?)";
    private static final String SQL_DELETE_CONNECTION_CERTIFICATE_TAG = "DELETE FROM certificate_tag WHERE certificateId = ? AND tagId = ?";
    private static final String SQL_SELECT_CERTIFICATE_ID_BY_TAG_ID = "SELECT certificateId FROM certificate_tag WHERE tagId = ?";

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public RelationRepositoryImpl(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public Long createRelationBetweenTagAndGiftCertificate(Long tagId, Long CertificateId) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
                connection -> {
                    PreparedStatement ps = connection.prepareStatement(SQL_INSERT_CONNECTION_CERTIFICATE_TAG, new String[]{"id"});
                    ps.setLong(1, CertificateId);
                    ps.setLong(2, tagId);
                    return ps;
                },
                keyHolder);
        return keyHolder.getKey().longValue();
    }

    @Override
    public boolean deleteRelationBetweenTagAndGiftCertificate(Long tagId, Long CertificateId) {
        return 1 == jdbcTemplate.update(
                connection -> {
                    PreparedStatement ps = connection.prepareStatement(SQL_DELETE_CONNECTION_CERTIFICATE_TAG);
                    ps.setLong(1, CertificateId);
                    ps.setLong(2, tagId);
                    return ps;
                });
    }

    @Override
    public List<Long> findListCertificateIdByTagId(Long tagId) {
        return jdbcTemplate.queryForList(SQL_SELECT_CERTIFICATE_ID_BY_TAG_ID, Long.class, tagId);
    }
}