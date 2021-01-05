package com.epam.esm.model.dao;

import com.epam.esm.entity.SprOperation;
import com.epam.esm.exception.DaoException;

import java.util.List;

public interface SprOperationDao extends Dao<SprOperation> {
    /**
     * Find a list of operations
     *
     * @return list of operations
     * @throws DaoException database access error or other errors
     */
    List<SprOperation> findAll() throws DaoException;
}
