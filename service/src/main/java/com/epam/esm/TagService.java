package com.epam.esm;

import com.epam.esm.model.TagDTO;

import java.util.List;

public interface TagService {
    /**
     * Search TagDTO by ID
     *
     * @param id - ID
     * @return TagDTO
     */
    TagDTO findById(Long id);

    /**
     * Create TagDTO
     *
     * @param tagDTO - TagDTO
     * @return true on successful createCheckEn
     */
    Long create(TagDTO tagDTO);

    /**
     * Delete TagDTO
     *
     * @param id - TagDTO id
     * @return true on successful delete
     */
    boolean delete(Long id);

    /**
     * Get tagDTO List
     *
     * @return tagDTO List
     */
    List<TagDTO> findAll();
}