package com.epam.esm.model.dao;

import com.epam.esm.exception.DaoException;

import java.sql.Connection;
import java.util.List;

public interface Dao<T> {
    /**
     * Get a list of Objects
     *
     * @return list of Objects
     * @throws DaoException database access error or other errors.
     */
    List<T> findAll() throws DaoException;

    /**
     * Object search by ID
     *
     * @param id - ID
     * @return object
     * @throws DaoException database access error or other errors.
     */
    T findById(Long id) throws DaoException;

    /**
     * Create Object
     *
     * @param t - Object
     * @return true on successful creation
     * @throws DaoException database access error or other errors
     */
    Long create(T t) throws DaoException;

    /**
     * Update Object
     *
     * @param t - Object
     * @return true on successful change
     * @throws DaoException database access error or other errors
     */
    boolean update(T t) throws DaoException;

    /**
     * Delete Object
     *
     * @param t - Object
     * @return true on successful delete
     * @throws DaoException database access error or other errors.
     */
    boolean delete(T t) throws DaoException;

    /**
     * Establishing a connection
     *
     * @param connection - connection
     */
    void setConnection(Connection connection);
}
