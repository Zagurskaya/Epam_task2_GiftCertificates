package com.epam.esm.model.service.impl;

import com.epam.esm.entity.UserEntry;
import com.epam.esm.exception.DaoException;
import com.epam.esm.exception.ServiceException;
import com.epam.esm.model.dao.UserEntryDao;
import com.epam.esm.model.dao.impl.UserEntryDaoImpl;
import com.epam.esm.model.service.EntityTransaction;
import com.epam.esm.model.service.UserEntryService;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDate;
import java.util.List;


public class UserEntryServiceImpl implements UserEntryService {

    private static final Logger logger = LogManager.getLogger(UserEntryServiceImpl.class);

    @Override
    public List<UserEntry> findAllToday() throws ServiceException {
        LocalDate date = LocalDate.now();
        UserEntryDao userEntryDao = new UserEntryDaoImpl();
        EntityTransaction transaction = new EntityTransaction();
        transaction.initSingleQuery(userEntryDao);
        try {
            return userEntryDao.findAllByData(date);
        } catch (DaoException e) {
            logger.log(Level.ERROR, "Database exception during fiend all user", e);
            throw new ServiceException("Database exception during fiend all user", e);
        } finally {
            transaction.endSingleQuery();
        }
    }


}
