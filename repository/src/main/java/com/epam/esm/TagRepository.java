package com.epam.esm;

import com.epam.esm.model.Tag;

import java.util.List;
import java.util.Optional;

public interface TagRepository {
    /**
     * Get a list of Tags
     *
     * @return list of Tags
     */
    List<Tag> findAll();

    /**
     * Tag search by ID
     *
     * @param id - ID
     * @return object
     */
    Tag findById(Long id);

    /**
     * Create Tag
     *
     * @param tag - Tag
     * @return true on successful creation
     */
    Long create(Tag tag);


    /**
     * Delete tag
     *
     * @param id - tag id
     * @return true on successful delete
     */
    boolean delete(Long id);

    /**
     * Get a list of Tags by GiftCertificateId
     *
     * @return list of Tags
     */
    List<Tag> findListTagsByCertificateId(Long certificateId);


    /**
     * Tag search by name
     *
     * @param name - tag name
     * @return tag
     */
    Optional<Tag> findByName(String name);
}