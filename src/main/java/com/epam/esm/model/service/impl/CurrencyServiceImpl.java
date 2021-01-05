package com.epam.esm.model.service.impl;

import com.epam.esm.controller.command.AttributeName;
import com.epam.esm.entity.Currency;
import com.epam.esm.exception.CommandException;
import com.epam.esm.exception.DaoException;
import com.epam.esm.exception.DaoConstraintViolationException;
import com.epam.esm.exception.ServiceException;
import com.epam.esm.model.dao.CurrencyDao;
import com.epam.esm.model.dao.impl.CurrencyDaoImpl;
import com.epam.esm.model.service.EntityTransaction;
import com.epam.esm.model.service.CurrencyService;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class CurrencyServiceImpl implements CurrencyService {

    private static final Logger logger = LogManager.getLogger(CurrencyServiceImpl.class);

    @Override
    public Currency findById(Long id) throws ServiceException {
        CurrencyDao currencyDao = new CurrencyDaoImpl();
        EntityTransaction transaction = new EntityTransaction();
        transaction.initSingleQuery(currencyDao);
        try {
            return currencyDao.findById(id);
        } catch (DaoException e) {
            logger.log(Level.ERROR, "Database exception during fiend currency by id", e);
            throw new ServiceException("Database exception during fiend currency by id", e);
        } finally {
            transaction.endSingleQuery();
        }
    }

    @Override
    public boolean create(Currency currency) throws ServiceException, CommandException {
        CurrencyDao currencyDao = new CurrencyDaoImpl();
        EntityTransaction transaction = new EntityTransaction();
        transaction.initSingleQuery(currencyDao);
        try {
            return currencyDao.create(currency) != 0L;
        } catch (DaoConstraintViolationException e) {
            logger.log(Level.ERROR, "Duplicate data currency ", e);
            throw new CommandException("Duplicate data currency ", e);
        } catch (DaoException e) {
            logger.log(Level.ERROR, "Database exception during createCheckEn currency ", e);
            throw new ServiceException("Database exception during createCheckEn currency ", e);
        } finally {
            transaction.endSingleQuery();
        }
    }

    @Override
    public boolean update(Currency currency) throws ServiceException, CommandException {
        CurrencyDao currencyDao = new CurrencyDaoImpl();
        EntityTransaction transaction = new EntityTransaction();
        transaction.initSingleQuery(currencyDao);
        try {
            return currencyDao.update(currency);
        } catch (DaoConstraintViolationException e) {
            logger.log(Level.ERROR, "Duplicate data currency ", e);
            throw new CommandException("Duplicate data currency ", e);
        } catch (DaoException e) {
            logger.log(Level.ERROR, "Database exception during update currency ", e);
            throw new ServiceException("Database exception during update currency ", e);
        } finally {
            transaction.endSingleQuery();
        }
    }

    @Override
    public boolean delete(Currency currency) throws ServiceException {
        CurrencyDao currencyDao = new CurrencyDaoImpl();
        EntityTransaction transaction = new EntityTransaction();
        transaction.initSingleQuery(currencyDao);
        try {
            return currencyDao.delete(currency);
        } catch (DaoException e) {
            logger.log(Level.ERROR, "Database exception during delete currency ", e);
            throw new ServiceException("Database exception during delete currency ", e);
        } finally {
            transaction.endSingleQuery();
        }
    }

    @Override
    public int countRows() throws ServiceException {
        CurrencyDao currencyDao = new CurrencyDaoImpl();
        EntityTransaction transaction = new EntityTransaction();
        transaction.initSingleQuery(currencyDao);
        try {
            return currencyDao.countRows();
        } catch (DaoException e) {
            logger.log(Level.ERROR, "Database exception during fiend count currencies row", e);
            throw new ServiceException("Database exception during fiend count currencies row", e);
        } finally {
            transaction.endSingleQuery();
        }
    }

    @Override
    public List<Currency> onePartOfListOnPage(int page) throws ServiceException {
        List currencies = new ArrayList();
        CurrencyDao currencyDao = new CurrencyDaoImpl();
        EntityTransaction transaction = new EntityTransaction();
        transaction.initSingleQuery(currencyDao);
        try {
            int recordsPerPage = AttributeName.RECORDS_PER_PAGE;
            int startRecord = (int) Math.ceil((page - 1) * recordsPerPage);
            currencies.addAll(currencyDao.findAll(recordsPerPage, startRecord));
            return currencies;
        } catch (DaoException e) {
            logger.log(Level.ERROR, "Database exception during fiend all currency", e);
            throw new ServiceException("Database exception during fiend all currency", e);
        } finally {
            transaction.endSingleQuery();
        }
    }

    @Override
    public List<Currency> findAll() throws ServiceException {
        CurrencyDao currencyDao = new CurrencyDaoImpl();
        EntityTransaction transaction = new EntityTransaction();
        transaction.initSingleQuery(currencyDao);
        try {
            return currencyDao.findAll();
        } catch (DaoException e) {
            logger.log(Level.ERROR, "Database exception during fiend all currency", e);
            throw new ServiceException("Database exception during fiend all currency", e);
        } finally {
            transaction.endSingleQuery();
        }
    }

    @Override
    public Map<String, String> findAllToMap() throws ServiceException {
        Map<String, String> currencyMap = new HashMap<>();
        List<Currency> currencies = findAll();
        currencies.forEach(currency -> {
            currencyMap.put(currency.getId().toString(), currency.getIso());
        });
        return currencyMap;
    }

    @Override
    public List findAllCurrenciesForSaleAndBuy() throws ServiceException {
        CurrencyDao currencyDao = new CurrencyDaoImpl();
        EntityTransaction transaction = new EntityTransaction();
        transaction.initSingleQuery(currencyDao);
        try {
            return currencyDao.findAllSKV();
        } catch (DaoException e) {
            logger.log(Level.ERROR, "Database exception during fiend all SKV currency", e);
            throw new ServiceException("Database exception during fiend all SKV currency", e);
        } finally {
            transaction.endSingleQuery();
        }
    }

}
