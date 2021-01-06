package com.epam.esm.model.service;

import com.epam.esm.exception.ServiceException;

import java.util.List;

public interface Service<T> {
    /**
     * Get Object List
     *
     * @return Object List
     */
    List<T> findAll() throws ServiceException;

    /**
     * Search Object by ID
     *
     * @param id - ID
     * @return Object
     * @throws ServiceException error during execution of logical blocks and actions
     */
    T findById(Long id) throws ServiceException;

    /**
     * Create Object
     *
     * @param t - Object
     * @return true on successful createCheckEn
     * @throws ServiceException error during execution of logical blocks and actions
     */
    boolean create(T t) throws ServiceException;

    /**
     * Update Object
     *
     * @param t - Object
     * @return true on successful update
     * @throws ServiceException error during execution of logical blocks and actions
     */
    boolean update(T t) throws ServiceException;

    /**
     * Delete Object
     *
     * @param t - Object
     * @return true on successful delete
     * @throws ServiceException error during execution of logical blocks and actions
     */
    boolean delete(T t) throws ServiceException;

}
