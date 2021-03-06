package com.epam.esm.model.service.impl;

import com.epam.esm.entity.Tag;
import com.epam.esm.exception.DaoException;
import com.epam.esm.exception.ServiceException;
import com.epam.esm.model.dao.TagDao;
import com.epam.esm.model.dao.impl.TagDaoImpl;
import com.epam.esm.model.service.EntityTransaction;
import com.epam.esm.model.service.TagService;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;


public class TagServiceImpl implements TagService {

    private static final Logger logger = LogManager.getLogger(TagServiceImpl.class);

    @Override
    public Tag findById(Long id) throws ServiceException {
        TagDao tagDao = new TagDaoImpl();
        EntityTransaction transaction = new EntityTransaction();
        transaction.initSingleQuery(tagDao);
        try {
            return tagDao.findById(id);
        } catch (DaoException e) {
            logger.log(Level.ERROR, "Database exception during fiend tag by id", e);
            throw new ServiceException("Database exception during fiend tag by id", e);
        } finally {
            transaction.endSingleQuery();
        }
    }

    @Override
    public boolean create(Tag tag) throws ServiceException {
        TagDao tagDao = new TagDaoImpl();
        EntityTransaction transaction = new EntityTransaction();
        transaction.initSingleQuery(tagDao);
        try {
            if (tagDao.findByName(tag.getName()) == null) {
                return tagDao.create(tag) != 0L;
            } else {
                logger.log(Level.ERROR, "Duplicate data tag's login ");
                throw new ServiceException("Duplicate data tag's login ");
            }
        }  catch (DaoException e) {
            logger.log(Level.ERROR, "Database exception during createCheckEn tag ", e);
            throw new ServiceException("Database exception during createCheckEn tag ", e);
        } finally {
            transaction.endSingleQuery();
        }
    }

    @Override
    public boolean update(Tag tag) throws ServiceException {
        TagDao tagDao = new TagDaoImpl();
        EntityTransaction transaction = new EntityTransaction();
        transaction.initSingleQuery(tagDao);
        try {
            return tagDao.update(tag);
        } catch (DaoException e) {
            logger.log(Level.ERROR, "Database exception during update tag ", e);
            throw new ServiceException("Database exception during update tag ", e);
        } finally {
            transaction.endSingleQuery();
        }
    }

    @Override
    public boolean delete(Tag tag) throws ServiceException {
        TagDao tagDao = new TagDaoImpl();
        EntityTransaction transaction = new EntityTransaction();
        transaction.initSingleQuery(tagDao);
        try {
            return tagDao.delete(tag);
        } catch (DaoException e) {
            logger.log(Level.ERROR, "Database exception during delete tag ", e);
            throw new ServiceException("Database exception during delete tag ", e);
        } finally {
            transaction.endSingleQuery();
        }
    }

    public List<Tag> findAll() throws ServiceException {
        TagDao tagDao = new TagDaoImpl();
        EntityTransaction transaction = new EntityTransaction();
        transaction.initSingleQuery(tagDao);
        try {
            return tagDao.findAll();
        } catch (DaoException e) {
            logger.log(Level.ERROR, "Database exception during fiend all tag", e);
            throw new ServiceException("Database exception during fiend all tag", e);
        } finally {
            transaction.endSingleQuery();
        }
    }
}
