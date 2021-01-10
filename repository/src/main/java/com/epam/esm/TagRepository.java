package com.epam.esm;

import com.epam.esm.exception.DaoException;
import com.epam.esm.model.Tag;

import java.sql.Connection;
import java.util.List;

public interface TagRepository {
    /**
     * Get a list of Tags
     *
     * @return list of Tags
     * @throws DaoException database access error or other errors.
     */
    List<Tag> findAll(Connection connection) throws DaoException;

    /**
     * Tag search by ID
     *
     * @param id - ID
     * @return object
     * @throws DaoException database access error or other errors.
     */
    Tag findById(Connection connection, Long id) throws DaoException;

    /**
     * Create Tag
     *
     * @param tag - Tag
     * @return true on successful creation
     * @throws DaoException database access error or other errors
     */
    Long create(Connection connection, Tag tag) throws DaoException;

    /**
     * Update Tag
     *
     * @param tag - Tag
     * @return true on successful change
     * @throws DaoException database access error or other errors
     */
    boolean update(Connection connection, Tag tag) throws DaoException;

    /**
     * Delete tag
     *
     * @param id - tag id
     * @return true on successful delete
     * @throws DaoException database access error or other errors.
     */
    boolean delete(Connection connection, Long id) throws DaoException;

    /**
     * Get a list of Tags by GiftCertificateId
     *
     * @return list of Tags
     * @throws DaoException database access error or other errors.
     */
    List<Tag> findListTagsByCertificateId(Connection connection, Long certificateId) throws DaoException;


    /**
     * Tag search by name
     *
     * @param name - tag name
     * @return tag
     * @throws DaoException database access error or other errors.
     */
    Tag findByName(Connection connection, String name) throws DaoException;

    /**
     * Create Tag and connection with GiftCertificate
     *
     * @param tagId         - Tag id
     * @param CertificateId - GiftCertificate Id
     * @return true on successful creation
     * @throws DaoException database access error or other errors
     */
    Long createConnectionBetweenTagAndGiftCertificate(Connection connection, Long tagId, Long CertificateId);

    /**
     * Delete Tag connection with GiftCertificate
     *
     * @param tagId         - Tag id
     * @param CertificateId - GiftCertificate Id
     * @return true on successful creation
     * @throws DaoException database access error or other errors
     */
    Long deleteConnectionBetweenTagAndGiftCertificate(Connection connection, Long tagId, Long CertificateId);
}