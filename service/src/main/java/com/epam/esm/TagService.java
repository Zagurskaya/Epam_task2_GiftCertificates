package com.epam.esm;

import com.epam.esm.exception.ServiceException;
import com.epam.esm.model.TagDTO;

import java.util.List;

public interface TagService {
    /**
     * Search Tag by ID
     *
     * @param id - ID
     * @return Tag
     * @throws ServiceException error during execution of logical blocks and actions
     */
    TagDTO findById(Long id) throws ServiceException;

    /**
     * Create Tag
     *
     * @param tagDTO - Tag
     * @return true on successful createCheckEn
     * @throws ServiceException error during execution of logical blocks and actions
     */
    Long create(TagDTO tagDTO) throws ServiceException;

    /**
     * Update Tag
     *
     * @param tagDTO - Tag
     * @return true on successful update
     * @throws ServiceException error during execution of logical blocks and actions
     */
    boolean update(TagDTO tagDTO) throws ServiceException;

    /**
     * Delete Tag
     *
     * @param id - Tag id
     * @return true on successful delete
     * @throws ServiceException error during execution of logical blocks and actions
     */
    boolean delete(Long id) throws ServiceException;

    /**
     * Get tagDTO List
     *
     * @return tagDTO List
     */
    List<TagDTO> findAll() throws ServiceException;
}