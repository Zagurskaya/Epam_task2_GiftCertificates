package com.epam.esm.impl;

import com.epam.esm.TagRepository;
import com.epam.esm.model.Tag;
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
import java.util.List;

@Repository
public class TagRepositoryImpl implements TagRepository {

    JdbcTemplate jdbcTemplate;

    private static final String SQL_SELECT_ALL_TAGS = "SELECT id, name FROM tag ";
    private static final String SQL_SELECT_TAG_BY_ID = "SELECT id, name FROM tag WHERE id= ? ";
    private static final String SQL_SELECT_TAG_BY_GIFT_CERTIFICATE_ID =
            "SELECT tag.id, tag.name FROM\n" +
                    "certificate_tag LEFT JOIN tag\n" +
                    "ON certificate_tag.tagId = tag.id\n" +
                    "WHERE certificate_tag.certificateId = ? ";
    private static final String SQL_SELECT_TAG_BY_NAME = "SELECT id, name FROM tag WHERE name = ? ";
    private static final String SQL_INSERT_TAG = "INSERT INTO tag(name) VALUES (?)";
    private static final String SQL_INSERT_CONNECTION_CERTIFICATE_TAG = "INSERT INTO certificate_tag(certificateId, tagId) VALUES (?, ?)";
    private static final String SQL_DELETE_CONNECTION_CERTIFICATE_TAG = "DELETE FROM certificate_tag WHERE certificateId = ? AND tagId = ?";
    private static final String SQL_DELETE_TAG = "DELETE FROM tag WHERE id=?";

    @Autowired
    public TagRepositoryImpl(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public List<Tag> findAll() {
        List<Tag> result = jdbcTemplate.query(SQL_SELECT_ALL_TAGS, new TagRowMapper());
        return result;
    }

    @Override
    public Tag findById(Long id) {
        List<Tag> result = jdbcTemplate.query(SQL_SELECT_TAG_BY_ID, new TagRowMapper(), id);
        return result.isEmpty() ? null : result.get(0);
    }

    @Override
    public Long create(Tag tag) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
                connection -> {
                    PreparedStatement ps = connection.prepareStatement(SQL_INSERT_TAG, new String[]{"id"});
                    ps.setString(1, tag.getName());
                    return ps;
                },
                keyHolder);
        return keyHolder.getKey().longValue();
    }

    @Override
    public boolean delete(Long id) {
        return 1 == jdbcTemplate.update(SQL_DELETE_TAG, id);
    }

    @Override
    public List<Tag> findListTagsByCertificateId(Long certificateId) {
        return jdbcTemplate.query(SQL_SELECT_TAG_BY_GIFT_CERTIFICATE_ID, new TagRowMapper(), certificateId);
    }

    @Override
    public Tag findByName(String name) {
        List<Tag> tags = jdbcTemplate.query(SQL_SELECT_TAG_BY_NAME, new TagRowMapper(), name);
        return tags.isEmpty() ? null : tags.get(0);
    }

    @Override
    public Long createConnectionBetweenTagAndGiftCertificate(Long tagId, Long CertificateId) {
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
    public boolean deleteConnectionBetweenTagAndGiftCertificate(Long tagId, Long CertificateId) {
        return 1 == jdbcTemplate.update(
                connection -> {
                    PreparedStatement ps = connection.prepareStatement(SQL_DELETE_CONNECTION_CERTIFICATE_TAG);
                    ps.setLong(1, CertificateId);
                    ps.setLong(2, tagId);
                    return ps;
                });
    }

    class TagRowMapper implements RowMapper<Tag> {

        @Override
        public Tag mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Tag(
                    rs.getLong(ColumnName.TAG_ID),
                    rs.getString(ColumnName.TAG_NAME)
            );
        }
    }
}