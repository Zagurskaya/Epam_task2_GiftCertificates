package com.epam.esm.model.service.impl;

import com.epam.esm.controller.command.AttributeName;
import com.epam.esm.entity.Currency;
import com.epam.esm.entity.RateCB;
import com.epam.esm.exception.CommandException;
import com.epam.esm.exception.DaoException;
import com.epam.esm.exception.DaoConstraintViolationException;
import com.epam.esm.exception.ServiceException;
import com.epam.esm.model.dao.CurrencyDao;
import com.epam.esm.model.dao.RateCBDao;
import com.epam.esm.model.dao.impl.CurrencyDaoImpl;
import com.epam.esm.model.dao.impl.RateCBDaoImpl;
import com.epam.esm.model.service.EntityTransaction;
import com.epam.esm.model.service.RateCBService;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


public class RateCBServiceImpl implements RateCBService {

    private static final Logger logger = LogManager.getLogger(RateCBServiceImpl.class);

    @Override
    public RateCB findById(Long id) throws ServiceException {
        RateCBDao rateCBDao = new RateCBDaoImpl();
        EntityTransaction transaction = new EntityTransaction();
        transaction.initSingleQuery(rateCBDao);
        try {
            return rateCBDao.findById(id);
        } catch (DaoException e) {
            logger.log(Level.ERROR, "Database exception during fiend rateCB by id", e);
            throw new ServiceException("Database exception during fiend rateCB by id", e);
        } finally {
            transaction.endSingleQuery();
        }
    }

    @Override
    public boolean create(RateCB rateCB) throws ServiceException, CommandException {
        RateCBDao rateCBDao = new RateCBDaoImpl();
        EntityTransaction transaction = new EntityTransaction();
        transaction.initSingleQuery(rateCBDao);
        try {
            return rateCBDao.create(rateCB) != 0L;
        } catch (DaoConstraintViolationException e) {
            logger.log(Level.ERROR, "Duplicate data rateCB ", e);
            throw new CommandException("Duplicate data rateCB ", e);
        } catch (DaoException e) {
            logger.log(Level.ERROR, "Database exception during createCheckEn rateCB ", e);
            throw new ServiceException("Database exception during createCheckEn rateCB ", e);
        } finally {
            transaction.endSingleQuery();
        }
    }

    @Override
    public void create(List<RateCB> rateCBList) throws ServiceException, CommandException {
        RateCBDao rateCBDao = new RateCBDaoImpl();
        EntityTransaction transaction = new EntityTransaction();
        transaction.init(rateCBDao);
        try {
            for (RateCB rateCB : rateCBList) {
                rateCBDao.create(rateCB);
                transaction.commit();
            }
        } catch (DaoConstraintViolationException e) {
            transaction.rollback();
            logger.log(Level.ERROR, "Duplicate data rateCB ", e);
            throw new CommandException("Duplicate data rateCB ", e);
        } catch (DaoException e) {
            transaction.rollback();
            logger.log(Level.ERROR, "Database exception during createCheckEn rateCB ", e);
            throw new ServiceException("Database exception during createCheckEn rateCB ", e);
        } finally {
            transaction.endSingleQuery();
        }
    }

    @Override
    public List<RateCB> allLastRateCB() throws ServiceException {
        List<RateCB> lastRate = new LinkedList<>();
        CurrencyDao currencyDao = new CurrencyDaoImpl();
        RateCBDao rateCBDao = new RateCBDaoImpl();
        EntityTransaction transaction = new EntityTransaction();
        transaction.init(rateCBDao, currencyDao);
        LocalDateTime now = LocalDateTime.now();
        BigDecimal buyRate;
        BigDecimal saleRate;
        try {
            List<Currency> currencies = currencyDao.findAll();
            for (Currency currency : currencies) {
                if (933L != currency.getId()) {
                    buyRate = rateCBDao.rateCBToday(now, currency.getId(), 933L);
                    lastRate.add(new RateCB.Builder()
                            .addСoming(currency.getId())
                            .addSpending(933L)
                            .addLocalDateTime(now)
                            .addSum(buyRate)
                            .addIsBack(false)
                            .build());
                    saleRate = rateCBDao.rateCBToday(now, 933L, currency.getId());
                    lastRate.add(new RateCB.Builder()
                            .addСoming(933L)
                            .addSpending(currency.getId())
                            .addLocalDateTime(now)
                            .addSum(saleRate)
                            .addIsBack(true)
                            .build());

                }
            }
            return lastRate;
        } catch (DaoException e) {
            transaction.rollback();
            logger.log(Level.ERROR, "Database exception during fiend all Las tRateCB", e);
            throw new ServiceException("Database exception during fiend  all Last RateCB", e);
        } finally {
            transaction.end();
        }
    }

    @Override
    public boolean update(RateCB rateCB) throws ServiceException, CommandException {
        RateCBDao rateCBDao = new RateCBDaoImpl();
        EntityTransaction transaction = new EntityTransaction();
        transaction.initSingleQuery(rateCBDao);
        try {
            return rateCBDao.update(rateCB);
        } catch (DaoConstraintViolationException e) {
            logger.log(Level.ERROR, "Duplicate data rateCB ", e);
            throw new CommandException("Duplicate data rateCB ", e);
        } catch (DaoException e) {
            logger.log(Level.ERROR, "Database exception during update rateCB ", e);
            throw new ServiceException("Database exception during update rateCB ", e);
        } finally {
            transaction.endSingleQuery();
        }
    }

    @Override
    public boolean delete(RateCB rateCB) throws ServiceException {
        RateCBDao rateCBDao = new RateCBDaoImpl();
        EntityTransaction transaction = new EntityTransaction();
        transaction.initSingleQuery(rateCBDao);
        try {
            return rateCBDao.delete(rateCB);
        } catch (DaoException e) {
            logger.log(Level.ERROR, "Database exception during delete rateCB ", e);
            throw new ServiceException("Database exception during delete rateCB ", e);
        } finally {
            transaction.endSingleQuery();
        }
    }

    @Override
    public int countRows() throws ServiceException {
        RateCBDao rateCBDao = new RateCBDaoImpl();
        EntityTransaction transaction = new EntityTransaction();
        transaction.initSingleQuery(rateCBDao);
        try {
            return rateCBDao.countRows();
        } catch (DaoException e) {
            logger.log(Level.ERROR, "Database exception during fiend count rateCBs row", e);
            throw new ServiceException("Database exception during fiend count rateCBs row", e);
        } finally {
            transaction.endSingleQuery();
        }
    }

    @Override
    public List<RateCB> onePartOfListOnPage(int page) throws ServiceException {
        List rateCBs = new ArrayList();
        RateCBDao rateCBDao = new RateCBDaoImpl();
        EntityTransaction transaction = new EntityTransaction();
        transaction.initSingleQuery(rateCBDao);
        try {
            int recordsPerPage = AttributeName.RECORDS_PER_PAGE;
            int startRecord = (int) Math.ceil((page - 1) * recordsPerPage);
            rateCBs.addAll(rateCBDao.findAll(recordsPerPage, startRecord));
            return rateCBs;
        } catch (DaoException e) {
            logger.log(Level.ERROR, "Database exception during fiend all rateCB", e);
            throw new ServiceException("Database exception during fiend all rateCB", e);
        } finally {
            transaction.endSingleQuery();
        }
    }

    @Override
    public BigDecimal rateCBToday(LocalDateTime now, Long coming, Long spending) throws ServiceException {
        RateCBDao rateCBDao = new RateCBDaoImpl();
        EntityTransaction transaction = new EntityTransaction();
        transaction.initSingleQuery(rateCBDao);
        try {
            return rateCBDao.rateCBToday(now, coming, spending);
        } catch (DaoException e) {
            logger.log(Level.ERROR, "Database exception during fiend rate CB Today", e);
            throw new ServiceException("Database exception during fiend rate CB Today", e);
        } finally {
            transaction.endSingleQuery();
        }
    }

}
