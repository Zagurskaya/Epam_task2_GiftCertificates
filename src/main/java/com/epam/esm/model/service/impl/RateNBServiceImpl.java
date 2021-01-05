package com.epam.esm.model.service.impl;

import com.epam.esm.controller.command.AttributeName;
import com.epam.esm.entity.RateNB;
import com.epam.esm.exception.CommandException;
import com.epam.esm.exception.DaoException;
import com.epam.esm.exception.DaoConstraintViolationException;
import com.epam.esm.exception.ServiceException;
import com.epam.esm.model.dao.RateNBDao;
import com.epam.esm.model.dao.impl.RateNBDaoImpl;
import com.epam.esm.model.service.EntityTransaction;
import com.epam.esm.model.service.RateNBService;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;


public class RateNBServiceImpl implements RateNBService {

    private static final Logger logger = LogManager.getLogger(RateNBServiceImpl.class);

    @Override
    public RateNB findById(Long id) throws ServiceException {
        RateNBDao rateNBDao = new RateNBDaoImpl();
        EntityTransaction transaction = new EntityTransaction();
        transaction.initSingleQuery(rateNBDao);
        try {
            return rateNBDao.findById(id);
        } catch (DaoException e) {
            logger.log(Level.ERROR, "Database exception during fiend rateNB by id", e);
            throw new ServiceException("Database exception during fiend rateNB by id", e);
        } finally {
            transaction.endSingleQuery();
        }
    }

    @Override
    public boolean create(RateNB rateNB) throws ServiceException, CommandException {
        RateNBDao rateNBDao = new RateNBDaoImpl();
        EntityTransaction transaction = new EntityTransaction();
        transaction.initSingleQuery(rateNBDao);
        try {
            return rateNBDao.create(rateNB) != 0L;
        } catch (DaoConstraintViolationException e) {
            logger.log(Level.ERROR, "Duplicate data rateNB ", e);
            throw new CommandException("Duplicate data rateNB ", e);
        } catch (DaoException e) {
            logger.log(Level.ERROR, "Database exception during createCheckEn rateNB ", e);
            throw new ServiceException("Database exception during createCheckEn rateNB ", e);
        } finally {
            transaction.endSingleQuery();
        }
    }

    @Override
    public void create(List<RateNB> rateNBList) throws ServiceException, CommandException {
        RateNBDao rateNBDao = new RateNBDaoImpl();
        EntityTransaction transaction = new EntityTransaction();
        transaction.init(rateNBDao);
        try {
            for (RateNB rateNB : rateNBList) {
                rateNBDao.create(rateNB);
                transaction.commit();
            }
        } catch (DaoConstraintViolationException e) {
            transaction.rollback();
            logger.log(Level.ERROR, "Duplicate data rateNB ", e);
            throw new CommandException("Duplicate data rateNB ", e);
        } catch (DaoException e) {
            transaction.rollback();
            logger.log(Level.ERROR, "Database exception during createCheckEn rateNB ", e);
            throw new ServiceException("Database exception during createCheckEn rateNB ", e);
        } finally {
            transaction.endSingleQuery();
        }
    }

    @Override
    public boolean update(RateNB rateNB) throws ServiceException, CommandException {
        RateNBDao rateNBDao = new RateNBDaoImpl();
        EntityTransaction transaction = new EntityTransaction();
        transaction.initSingleQuery(rateNBDao);
        try {
            return rateNBDao.update(rateNB);
        } catch (DaoConstraintViolationException e) {
            logger.log(Level.ERROR, "Duplicate data rateNB ", e);
            throw new CommandException("Duplicate data rateNB ", e);
        } catch (DaoException e) {
            logger.log(Level.ERROR, "Database exception during update rateNB ", e);
            throw new ServiceException("Database exception during update rateNB ", e);
        } finally {
            transaction.endSingleQuery();
        }
    }

    @Override
    public boolean delete(RateNB rateNB) throws ServiceException {
        RateNBDao rateNBDao = new RateNBDaoImpl();
        EntityTransaction transaction = new EntityTransaction();
        transaction.initSingleQuery(rateNBDao);
        try {
            return rateNBDao.delete(rateNB);
        } catch (DaoException e) {
            logger.log(Level.ERROR, "Database exception during delete rateNB ", e);
            throw new ServiceException("Database exception during delete rateNB ", e);
        } finally {
            transaction.endSingleQuery();
        }
    }

    @Override
    public int countRows() throws ServiceException {
        RateNBDao rateNBDao = new RateNBDaoImpl();
        EntityTransaction transaction = new EntityTransaction();
        transaction.initSingleQuery(rateNBDao);
        try {
            return rateNBDao.countRows();
        } catch (DaoException e) {
            logger.log(Level.ERROR, "Database exception during fiend count rateNBs row", e);
            throw new ServiceException("Database exception during fiend count rateNBs row", e);
        } finally {
            transaction.endSingleQuery();
        }
    }

    @Override
    public List<RateNB> onePartOfListOnPage(int page) throws ServiceException {
        List rateNBs = new ArrayList();
        RateNBDao rateNBDao = new RateNBDaoImpl();
        EntityTransaction transaction = new EntityTransaction();
        transaction.initSingleQuery(rateNBDao);
        try {
            int recordsPerPage = AttributeName.RECORDS_PER_PAGE;
            int startRecord = (int) Math.ceil((page - 1) * recordsPerPage);
            rateNBs.addAll(rateNBDao.findAll(recordsPerPage, startRecord));
            return rateNBs;
        } catch (DaoException e) {
            logger.log(Level.ERROR, "Database exception during fiend all rateNB", e);
            throw new ServiceException("Database exception during fiend all rateNB", e);
        } finally {
            transaction.endSingleQuery();
        }
    }
}
