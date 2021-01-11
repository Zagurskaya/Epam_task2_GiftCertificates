package com.epam.esm;

import com.epam.esm.exception.ServiceException;
import com.epam.esm.model.TagDTO;

import java.util.List;

public interface TagService {
    /**
     * Search TagDTO by ID
     *
     * @param id - ID
     * @return TagDTO
     * @throws ServiceException error during execution of logical blocks and actions
     */
    TagDTO findById(Long id) throws ServiceException;

    /**
     * Create TagDTO
     *
     * @param tagDTO - TagDTO
     * @return true on successful createCheckEn
     * @throws ServiceException error during execution of logical blocks and actions
     */
    Long create(TagDTO tagDTO) throws ServiceException;

    /**
     * Delete TagDTO
     *
     * @param id - TagDTO id
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