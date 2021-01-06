package com.epam.esm.model.service.impl;

import com.epam.esm.controller.AttributeName;
import com.epam.esm.entity.User;
import com.epam.esm.exception.CommandException;
import com.epam.esm.exception.DaoConstraintViolationException;
import com.epam.esm.model.dao.UserDao;
import com.epam.esm.model.dao.impl.UserDaoImpl;
import com.epam.esm.exception.DaoException;
import com.epam.esm.exception.ServiceException;
import com.epam.esm.model.service.EntityTransaction;
import com.epam.esm.model.service.UserService;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;


public class UserServiceImpl implements UserService {

    private static final Logger logger = LogManager.getLogger(UserServiceImpl.class);
    private static final String HASH_ALGORITHM = "SHA-512";

    @Override
    public User findUserByLoginAndValidPassword(String login, String password) throws ServiceException {
        UserDao userDao = new UserDaoImpl();
        EntityTransaction transaction = new EntityTransaction();
        transaction.initSingleQuery(userDao);
        User user;
        try {
            user = userDao.findByLogin(login);
            String userPassword = userDao.findPasswordByLogin(login);
            User returnUser = user != null && userPassword.equals(getHash(password)) ? user : null;
            return returnUser;
        } catch (DaoException e) {
            logger.log(Level.ERROR, "Database exception during fiend user by login and password", e);
            throw new ServiceException("Database exception during fiend user by login and password", e);
        } finally {
            transaction.endSingleQuery();
        }
    }

    @Override
    public User findById(Long id) throws ServiceException {
        UserDao userDao = new UserDaoImpl();
        EntityTransaction transaction = new EntityTransaction();
        transaction.initSingleQuery(userDao);
        try {
            return userDao.findById(id);
        } catch (DaoException e) {
            logger.log(Level.ERROR, "Database exception during fiend user by id", e);
            throw new ServiceException("Database exception during fiend user by id", e);
        } finally {
            transaction.endSingleQuery();
        }
    }

    @Override
    public boolean create(User user) throws ServiceException, CommandException {
        UserDao userDao = new UserDaoImpl();
        EntityTransaction transaction = new EntityTransaction();
        transaction.initSingleQuery(userDao);
        try {
            if (userDao.findByLogin(user.getLogin()) == null) {
                return userDao.create(user) != 0L;
            } else {
                logger.log(Level.ERROR, "Duplicate data user's login ");
                throw new CommandException("Duplicate data user's login ");
            }
        } catch (DaoConstraintViolationException e) {
            logger.log(Level.ERROR, "Duplicate data user ", e);
            throw new CommandException("Duplicate data user ", e);
        } catch (DaoException e) {
            logger.log(Level.ERROR, "Database exception during createCheckEn user ", e);
            throw new ServiceException("Database exception during createCheckEn user ", e);
        } finally {
            transaction.endSingleQuery();
        }
    }

    @Override
    public boolean create(User user, String password) throws ServiceException, CommandException {
        UserDao userDao = new UserDaoImpl();
        EntityTransaction transaction = new EntityTransaction();
        transaction.initSingleQuery(userDao);
        try {
            if (userDao.findByLogin(user.getLogin()) == null) {
                String hashPassword = getHash(password);
                return userDao.create(user, hashPassword) != 0L;
            } else {
                logger.log(Level.ERROR, "Duplicate data user's login ");
                throw new CommandException("Duplicate data user's login ");
            }
        } catch (DaoConstraintViolationException e) {
            logger.log(Level.ERROR, "Duplicate data user ", e);
            throw new CommandException("Duplicate data user ", e);
        } catch (DaoException e) {
            logger.log(Level.ERROR, "Database exception during createCheckEn user ", e);
            throw new ServiceException("Database exception during createCheckEn user ", e);
        } finally {
            transaction.endSingleQuery();
        }
    }

    @Override
    public boolean update(User user) throws ServiceException, CommandException {
        UserDao userDao = new UserDaoImpl();
        EntityTransaction transaction = new EntityTransaction();
        transaction.initSingleQuery(userDao);
        try {
            return userDao.update(user);
        } catch (DaoConstraintViolationException e) {
            logger.log(Level.ERROR, "Duplicate data user ", e);
            throw new CommandException("Duplicate data user ", e);
        } catch (DaoException e) {
            logger.log(Level.ERROR, "Database exception during update user ", e);
            throw new ServiceException("Database exception during update user ", e);
        } finally {
            transaction.endSingleQuery();
        }
    }

    @Override
    public boolean delete(User user) throws ServiceException {
        UserDao userDao = new UserDaoImpl();
        EntityTransaction transaction = new EntityTransaction();
        transaction.initSingleQuery(userDao);
        try {
            return userDao.delete(user);
        } catch (DaoException e) {
            logger.log(Level.ERROR, "Database exception during delete user ", e);
            throw new ServiceException("Database exception during delete user ", e);
        } finally {
            transaction.endSingleQuery();
        }
    }

    public List<User> findAll() throws ServiceException {
        UserDao userDao = new UserDaoImpl();
        EntityTransaction transaction = new EntityTransaction();
        transaction.initSingleQuery(userDao);
        try {
            return userDao.findAll();
        } catch (DaoException e) {
            logger.log(Level.ERROR, "Database exception during fiend all user", e);
            throw new ServiceException("Database exception during fiend all user", e);
        } finally {
            transaction.endSingleQuery();
        }
    }

    private static String getHash(String password) throws ServiceException {
        StringBuilder hashPassword;
        try {
            MessageDigest messageDigest = MessageDigest.getInstance(HASH_ALGORITHM);
            byte[] bytesMessageDigest = messageDigest.digest(password.getBytes());
            BigInteger no = new BigInteger(1, bytesMessageDigest);
            hashPassword = new StringBuilder(no.toString(16));
            while (hashPassword.length() < 32) {
                hashPassword.insert(0, "0");
            }

        } catch (NoSuchAlgorithmException e) {
            logger.log(Level.ERROR, "No such algorithm " + HASH_ALGORITHM, e);
            throw new ServiceException("No such algorithm " + HASH_ALGORITHM, e);
        }
        return hashPassword.toString();
    }
}
