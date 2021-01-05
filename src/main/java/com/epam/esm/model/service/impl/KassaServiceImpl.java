package com.epam.esm.model.service.impl;

import com.epam.esm.controller.command.AttributeName;
import com.epam.esm.entity.Duties;
import com.epam.esm.entity.Kassa;
import com.epam.esm.entity.SprEntry;
import com.epam.esm.entity.User;
import com.epam.esm.exception.CommandException;
import com.epam.esm.exception.DaoException;
import com.epam.esm.exception.DaoConstraintViolationException;
import com.epam.esm.exception.NegativeBalanceException;
import com.epam.esm.exception.ServiceException;
import com.epam.esm.model.dao.KassaDao;
import com.epam.esm.model.dao.SprEntryDao;
import com.epam.esm.model.dao.impl.KassaDaoImpl;
import com.epam.esm.model.dao.impl.SprEntryDaoImpl;
import com.epam.esm.model.service.EntityTransaction;
import com.epam.esm.model.service.KassaService;
import com.epam.esm.util.DataUtil;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


public class KassaServiceImpl implements KassaService {

    private static final Logger logger = LogManager.getLogger(KassaServiceImpl.class);

    @Override
    public Kassa findById(Long id) throws ServiceException {
        KassaDao kassaDao = new KassaDaoImpl();
        EntityTransaction transaction = new EntityTransaction();
        transaction.initSingleQuery(kassaDao);
        try {
            return kassaDao.findById(id);
        } catch (DaoException e) {
            logger.log(Level.ERROR, "Database exception during fiend kassa by id", e);
            throw new ServiceException("Database exception during fiend kassa by id", e);
        } finally {
            transaction.endSingleQuery();
        }
    }

    @Override
    public boolean create(Kassa kassa) throws ServiceException, CommandException {
        KassaDao kassaDao = new KassaDaoImpl();
        EntityTransaction transaction = new EntityTransaction();
        transaction.initSingleQuery(kassaDao);
        try {
            return kassaDao.create(kassa) != null;
        } catch (DaoConstraintViolationException e) {
            logger.log(Level.ERROR, "Duplicate data kassa ", e);
            throw new CommandException("Duplicate data kassa ", e);
        } catch (DaoException e) {
            logger.log(Level.ERROR, "Database exception during createCheckEn kassa ", e);
            throw new ServiceException("Database exception during createCheckEn kassa ", e);
        } finally {
            transaction.endSingleQuery();
        }
    }

    @Override
    public boolean update(Kassa kassa) throws ServiceException, CommandException {
        KassaDao kassaDao = new KassaDaoImpl();
        EntityTransaction transaction = new EntityTransaction();
        transaction.initSingleQuery(kassaDao);
        try {
            return kassaDao.update(kassa);
        } catch (DaoConstraintViolationException e) {
            logger.log(Level.ERROR, "Duplicate data kassa ", e);
            throw new CommandException("Duplicate data kassa ", e);
        } catch (DaoException e) {
            logger.log(Level.ERROR, "Database exception during update kassa ", e);
            throw new ServiceException("Database exception during update kassa ", e);
        } finally {
            transaction.endSingleQuery();
        }
    }

    @Override
    public boolean delete(Kassa kassa) throws ServiceException {
        KassaDao kassaDao = new KassaDaoImpl();
        EntityTransaction transaction = new EntityTransaction();
        transaction.initSingleQuery(kassaDao);
        try {
            return kassaDao.delete(kassa);
        } catch (DaoException e) {
            logger.log(Level.ERROR, "Database exception during delete kassa ", e);
            throw new ServiceException("Database exception during delete kassa ", e);
        } finally {
            transaction.endSingleQuery();
        }
    }

    @Override
    public int countRows() throws ServiceException {
        KassaDao kassaDao = new KassaDaoImpl();
        EntityTransaction transaction = new EntityTransaction();
        transaction.initSingleQuery(kassaDao);
        try {
            return kassaDao.countRows();
        } catch (DaoException e) {
            logger.log(Level.ERROR, "Database exception during fiend count kassaList row", e);
            throw new ServiceException("Database exception during fiend count kassaList row", e);
        } finally {
            transaction.endSingleQuery();
        }
    }

    @Override
    public List<Kassa> onePartOfListOnPage(int page) throws ServiceException {
        List kassaList = new ArrayList();
        KassaDao kassaDao = new KassaDaoImpl();
        EntityTransaction transaction = new EntityTransaction();
        transaction.initSingleQuery(kassaDao);
        try {
            int recordsPerPage = AttributeName.RECORDS_PER_PAGE;
            int startRecord = (int) Math.ceil((page - 1) * recordsPerPage);
            kassaList.addAll(kassaDao.findAll(recordsPerPage, startRecord));
            return kassaList;
        } catch (DaoException e) {
            logger.log(Level.ERROR, "Database exception during fiend all kassa", e);
            throw new ServiceException("Database exception during fiend all kassa", e);
        } finally {
            transaction.endSingleQuery();
        }
    }

    //внутрикассовые операции
    @Override
    public boolean updateKassaInnerOperation(LocalDate date, Long dutiesId, Long currencyId, BigDecimal sum, Long sprOperationsId) throws ServiceException, NegativeBalanceException {
        SprEntryDao sprEntryDao = new SprEntryDaoImpl();
        KassaDao kassaDao = new KassaDaoImpl();
        EntityTransaction transaction = new EntityTransaction();
        transaction.init(sprEntryDao, kassaDao);
        try {
            List<SprEntry> sprEntries = sprEntryDao.findAllBySprOperationIdAndCurrencyId(sprOperationsId, currencyId);

            Kassa kassa = kassaDao.findByCurrencyIdAndDateAndDutiesId(date, dutiesId, currencyId);
            BigDecimal kassasComing = kassa.getComing();
            BigDecimal kassasSpending = kassa.getSpending();
            BigDecimal kassaBalance = kassa.getBalance();

            Kassa.Builder updateKassaBuilder = new Kassa
                    .Builder()
                    .addId(kassa.getId())
                    .addCurrencyId(currencyId)
                    .addReceived(kassa.getReceived())
                    .addTransmitted(kassa.getTransmitted())
                    .addUserId(kassa.getUserId())
                    .addData(date)
                    .addDutiesId(dutiesId);

            Kassa updateKassa;
            if (sprEntries.get(0).getIsSpending()) {
                updateKassa = updateKassaBuilder
                        .addComing(kassa.getComing())
                        .addSpending(DataUtil.round(kassasSpending.add(sum)))
                        .addBalance(DataUtil.round(kassaBalance.subtract(sum)))
                        .build();
            } else {
                updateKassa = updateKassaBuilder
                        .addComing(DataUtil.round(kassasComing.add(sum)))
                        .addSpending(kassa.getSpending())
                        .addBalance(DataUtil.round(kassaBalance.add(sum)))
                        .build();
            }
            if (updateKassa.getBalance().compareTo(BigDecimal.ZERO) < 0) {
                throw new NegativeBalanceException("incorrect balance by currency " + updateKassa.getCurrencyId());
            }
            boolean result = kassaDao.update(updateKassa);
            transaction.commit();
            return result;
        } catch (DaoConstraintViolationException e) {
            logger.log(Level.ERROR, "Duplicate data kassa ", e);
            throw new ServiceException("Duplicate data kassa ", e);
        } catch (DaoException e) {
            transaction.rollback();
            logger.log(Level.ERROR, "Database exception during update Kassa InSide Operation", e);
            throw new ServiceException("Database exception during update Kassa InSide Operation", e);
        } finally {
            transaction.end();
        }
    }

    //внекассовые операции
    @Override
    public boolean updateKassaOuterOperation(LocalDate date, Long dutiesId, Long currencyId, BigDecimal sum, Long sprOperationsId) throws ServiceException {

        SprEntryDao sprEntryDao = new SprEntryDaoImpl();
        KassaDao kassaDao = new KassaDaoImpl();
        EntityTransaction transaction = new EntityTransaction();
        transaction.init(sprEntryDao, kassaDao);
        try {
            List<SprEntry> sprEntries = sprEntryDao.findAllBySprOperationIdAndCurrencyId(sprOperationsId, currencyId);

            Kassa kassa = kassaDao.findByCurrencyIdAndDateAndDutiesId(date, dutiesId, currencyId);

            BigDecimal kassasReceived = kassa.getReceived();
            BigDecimal kassasTransmitted = kassa.getTransmitted();
            BigDecimal kassaBalance = kassa.getBalance();

            Kassa.Builder updateKassaBuilder = new Kassa
                    .Builder()
                    .addId(kassa.getId())
                    .addCurrencyId(currencyId)
                    .addComing(kassa.getComing())
                    .addSpending(kassa.getSpending())
                    .addUserId(kassa.getUserId())
                    .addData(date)
                    .addDutiesId(dutiesId);

            Kassa updateKassa;
            if (sprEntries.get(0).getIsSpending()) {
                updateKassa = updateKassaBuilder
                        .addReceived(kassa.getReceived())
                        .addTransmitted(DataUtil.round(kassasTransmitted.add(sum)))
                        .addBalance(DataUtil.round(kassaBalance.subtract(sum)))
                        .build();
            } else {
                updateKassa = updateKassaBuilder
                        .addReceived(DataUtil.round(kassasReceived.add(sum)))
                        .addTransmitted(kassa.getTransmitted())
                        .addBalance(DataUtil.round(kassaBalance.add(sum)))
                        .build();
            }
            boolean result = kassaDao.update(updateKassa);
            transaction.commit();
            return result;
        } catch (DaoConstraintViolationException e) {
            logger.log(Level.ERROR, "Duplicate data kassa ", e);
            throw new ServiceException("Duplicate data kassa ", e);
        } catch (DaoException e) {
            transaction.rollback();
            logger.log(Level.ERROR, "Database exception during update Kassa Out Side Operation", e);
            throw new ServiceException("Database exception during update Kassa out Side Operation", e);
        } finally {
            transaction.end();
        }
    }

    @Override
    public List<Kassa> getBalance(User user, Duties duties) throws ServiceException {
        List kassaList = new ArrayList();
        KassaDao kassaDao = new KassaDaoImpl();
        EntityTransaction transaction = new EntityTransaction();
        transaction.initSingleQuery(kassaDao);
        try {
            kassaList.addAll(kassaDao.findAllByUserIdAndDutiesId(user.getId(), duties.getId()));
        } catch (DaoException e) {
            logger.log(Level.ERROR, "Database exception during getBalance", e);
            throw new ServiceException("Database exception during getBalance", e);
        } finally {
            transaction.endSingleQuery();
        }
        return kassaList;
    }

    @Override
    public boolean isBalanceValid(User user, Duties duties) throws ServiceException {
        List<Kassa> kassaList = getBalance(user, duties);
        for (Kassa kassa : kassaList) {
            if (kassa.getBalance().compareTo(BigDecimal.ZERO) != 0) {
                return false;
            }
        }
        return true;
    }

}
