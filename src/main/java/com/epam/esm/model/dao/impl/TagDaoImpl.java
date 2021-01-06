package com.epam.esm.model.dao.impl;

import com.epam.esm.entity.Tag;
import com.epam.esm.exception.DaoConstraintViolationException;
import com.epam.esm.exception.DaoException;
import com.epam.esm.model.dao.AbstractDao;
import com.epam.esm.model.dao.TagDao;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TagDaoImpl extends AbstractDao implements TagDao {

    private static final Logger logger = LogManager.getLogger(TagDaoImpl.class);

    private static final String SQL_SELECT_ALL_TAGS = "SELECT id, name FROM tag ";
    private static final String SQL_SELECT_TAG_BY_ID = "SELECT id, name FROM tag WHERE id= ? ";
    private static final String SQL_INSERT_TAG = "INSERT INTO tag(name) VALUES (?)";
    private static final String SQL_UPDATE_TAG = "UPDATE tag SET name=? WHERE id= ?";
    private static final String SQL_DELETE_TAG = "DELETE FROM tag WHERE id=?";

    @Override
    public List<Tag> findAll(int limit, int startPosition) throws DaoException {
        return null;
    }

    @Override
    public Tag findById(Long id) throws DaoException {
        Tag tag = null;
        try {
            try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_SELECT_TAG_BY_ID)) {
                preparedStatement.setLong(1, id);
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    String name = resultSet.getString(ColumnName.TAG_NAME);
                    tag = new Tag();
                    tag.setId(id);
                    tag.setName(name);
                }
            }
        } catch (SQLException e) {
            logger.log(Level.ERROR, "Database exception during fiend tag by id", e);
            throw new DaoException("Database exception during fiend tag by id", e);
        }
        return tag;
    }

    @Override
    public Long create(Tag tag) throws DaoException, DaoConstraintViolationException {
        int result;
        try {
            try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_INSERT_TAG, Statement.RETURN_GENERATED_KEYS)) {
                preparedStatement.setString(1, tag.getName());
                result = preparedStatement.executeUpdate();
                if (1 == result) {
                    ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
                    if (generatedKeys.next()) {
                        return generatedKeys.getLong(1);
                    }
                }
            }
        } catch (SQLIntegrityConstraintViolationException e) {
            throw new DaoConstraintViolationException("Duplicate data tag", e);
        } catch (SQLException e) {
            logger.log(Level.ERROR, "Database exception during createCheckEn tag", e);
            throw new DaoException("Database exception during createCheckEn tag", e);
        }
        return 0L;
    }
    
    @Override
    public boolean update(Tag tag) throws DaoException, DaoConstraintViolationException {
        int result;
        try {
            try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_UPDATE_TAG)) {
                preparedStatement.setString(1, tag.getName());
                preparedStatement.setLong(2, tag.getId());
                result = preparedStatement.executeUpdate();
            }
        } catch (SQLIntegrityConstraintViolationException e) {
            throw new DaoConstraintViolationException("Duplicate data tag", e);
        } catch (SQLException e) {
            logger.log(Level.ERROR, "Database exception during update tag", e);
            throw new DaoException("Database exception during update tag ", e);
        }
        return 1 == result;
    }

    @Override
    public boolean delete(Tag tag) throws DaoException {
        int result;
        try {
            try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_DELETE_TAG)) {
                preparedStatement.setLong(1, tag.getId());
                result = preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            logger.log(Level.ERROR, "Database exception during delete tag", e);
            throw new DaoException("Database exception during delete tag ", e);
        }
        return 1 == result;
    }

    @Override
    public int countRows() throws DaoException {
        int count=0;
        return count;
    }

//    @Override
    public List<Tag> findAll() throws DaoException {
        List<Tag> tags = new ArrayList<>();
        try {
            try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_SELECT_ALL_TAGS)) {
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    Long id = resultSet.getLong(ColumnName.TAG_ID);
                    String name = resultSet.getString(ColumnName.TAG_NAME);
                    Tag tag = new Tag();
                    tag.setId(id);
                    tag.setName(name);
                    tags.add(tag);
                }
            }
        } catch (SQLException e) {
            logger.log(Level.ERROR, "Database exception during fiend all tag", e);
            throw new DaoException("Database exception during fiend all tag", e);
        }
        return tags;
    }
}
