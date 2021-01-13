package com.epam.esm;

import com.epam.esm.exception.DaoException;
import com.epam.esm.model.Tag;

import java.util.List;

public interface TagRepository {
    /**
     * Get a list of Tags
     *
     * @return list of Tags
     * @throws DaoException database access error or other errors.
     */
    List<Tag> findAll();

    /**
     * Tag search by ID
     *
     * @param id - ID
     * @return object
     * @throws DaoException database access error or other errors.
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
    Tag findByName(String name);

    /**
     * Create Tag and connection with GiftCertificate
     *
     * @param tagId         - Tag id
     * @param CertificateId - GiftCertificate Id
     */
    Long createConnectionBetweenTagAndGiftCertificate(Long tagId, Long CertificateId);

    /**
     * Delete Tag connection with GiftCertificate
     *
     * @param tagId         - Tag id
     * @param CertificateId - GiftCertificate Id
     * @return true on successful creation
     */
    boolean deleteConnectionBetweenTagAndGiftCertificate(Long tagId, Long CertificateId);
}