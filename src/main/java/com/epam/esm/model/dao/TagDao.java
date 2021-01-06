package com.epam.esm.model.dao;

import com.epam.esm.entity.Tag;
import com.epam.esm.exception.DaoException;

public interface TagDao extends Dao<Tag> {

    /**
     * Tag search by name
     *
     * @param name - tag name
     * @return tag
     * @throws DaoException database access error or other errors.
     */
    Tag findByName(String name) throws DaoException;
}
