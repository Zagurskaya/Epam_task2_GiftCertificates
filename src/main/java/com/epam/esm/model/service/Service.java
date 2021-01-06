package com.epam.esm.model.service;

import com.epam.esm.exception.CommandException;
import com.epam.esm.exception.ServiceException;

import java.util.List;

public interface Service<T> {
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
     * @throws CommandException volition error
     */
    boolean create(T t) throws ServiceException, CommandException;

    /**
     * Update Object
     *
     * @param t - Object
     * @return true on successful update
     * @throws ServiceException error during execution of logical blocks and actions
     */
    boolean update(T t) throws ServiceException, CommandException;

    /**
     * Delete Object
     *
     * @param t - Object
     * @return true on successful delete
     * @throws ServiceException error during execution of logical blocks and actions
     */
    boolean delete(T t) throws ServiceException;

}
