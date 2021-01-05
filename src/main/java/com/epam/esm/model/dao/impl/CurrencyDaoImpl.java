package com.epam.esm.model.dao.impl;

import com.epam.esm.entity.Currency;
import com.epam.esm.exception.DaoException;
import com.epam.esm.exception.DaoConstraintViolationException;
import com.epam.esm.model.dao.AbstractDao;
import com.epam.esm.model.dao.CurrencyDao;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class CurrencyDaoImpl extends AbstractDao implements CurrencyDao {

    private static final Logger logger = LogManager.getLogger(CurrencyDaoImpl.class);

    private static final String SQL_SELECT_ALL_CURRENCIES_ON_PAGE = "SELECT id, iso, `nameRU`,`nameEN` FROM `currency`  ORDER BY id LIMIT ? Offset ? ";
    private static final String SQL_SELECT_ALL_CURRENCIES = "SELECT id, iso, `nameRU`,`nameEN` FROM `currency`  ORDER BY id ";
    private static final String SQL_SELECT_ALL_SKV_CURRENCIES = "SELECT id, iso, `nameRU`,`nameEN` FROM `currency`  where id<>933 ORDER BY id ";
    private static final String SQL_SELECT_CURRENCY_BY_ID = "SELECT id, iso, `nameRU`,`nameEN` FROM `currency` WHERE id= ? ";
    private static final String SQL_INSERT_CURRENCY = "INSERT INTO currency(id, iso, `nameRU`,`nameEN`) VALUES (?, ?, ?, ?)";
    private static final String SQL_UPDATE_CURRENCY = "UPDATE currency SET iso=?, `nameRU` =?,`nameEN` = ? WHERE id= ?";
    private static final String SQL_DELETE_CURRENCY = "DELETE FROM currency WHERE id=?";
    private static final String SQL_SELECT_COUNT_CURRENCIES = "SELECT COUNT(id) FROM `currency`";

    @Override
    public List<Currency> findAll(int limit, int startPosition) throws DaoException {
        List<Currency> currencies = new ArrayList<>();
        try {
            try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_SELECT_ALL_CURRENCIES_ON_PAGE)) {
                preparedStatement.setLong(1, limit);
                preparedStatement.setLong(2, startPosition);
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    Long id = resultSet.getLong(ColumnName.CURRENCY_ID);
                    String iso = resultSet.getString(ColumnName.CURRENCY_ISO);
                    String nameRU = resultSet.getString(ColumnName.CURRENCY_NAME_RU);
                    String nameEN = resultSet.getString(ColumnName.CURRENCY_NAME_EN);
                    Currency currency = new Currency.Builder()
                            .addId(id)
                            .addIso(iso)
                            .addNameRU(nameRU)
                            .addNameEN(nameEN)
                            .build();
                    currencies.add(currency);
                }
            }
        } catch (SQLException e) {
            logger.log(Level.ERROR, "Database exception during fiend all currency", e);
            throw new DaoException("Database exception during fiend all currency", e);
        }
        return currencies;
    }

    @Override
    public Currency findById(Long id) throws DaoException {
        Currency currency = null;
        try {
            try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_SELECT_CURRENCY_BY_ID)) {
                preparedStatement.setLong(1, id);
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    String iso = resultSet.getString(ColumnName.CURRENCY_ISO);
                    String nameRU = resultSet.getString(ColumnName.CURRENCY_NAME_RU);
                    String nameEN = resultSet.getString(ColumnName.CURRENCY_NAME_EN);
                    currency = new Currency.Builder()
                            .addId(id)
                            .addIso(iso)
                            .addNameRU(nameRU)
                            .addNameEN(nameEN)
                            .build();
                }
            }
        } catch (SQLException e) {
            logger.log(Level.ERROR, "Database exception during fiend currency by id", e);
            throw new DaoException("Database exception during fiend currency by id", e);
        }
        return currency;
    }

    @Override
    public Long create(Currency currency) throws DaoException, DaoConstraintViolationException {
        int result;
        try {
            try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_INSERT_CURRENCY, Statement.RETURN_GENERATED_KEYS)) {
                preparedStatement.setLong(1, currency.getId());
                preparedStatement.setString(2, currency.getIso());
                preparedStatement.setString(3, currency.getNameRU());
                preparedStatement.setString(4, currency.getNameEN());
                result = preparedStatement.executeUpdate();
                if (1 == result) {
                    ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
                    if (generatedKeys.next()) {
                        return generatedKeys.getLong(1);
                    }
                }
            }
        } catch (SQLIntegrityConstraintViolationException e) {
            throw new DaoConstraintViolationException("Duplicate data currency", e);
        } catch (SQLException e) {
            logger.log(Level.ERROR, "Database exception during createCheckEn currency", e);
            throw new DaoException("Database exception during createCheckEn currency", e);
        }
        return 0L;
    }

    @Override
    public boolean update(Currency currency) throws DaoException, DaoConstraintViolationException {
        int result;
        try {
            try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_UPDATE_CURRENCY)) {
                preparedStatement.setString(1, currency.getIso());
                preparedStatement.setString(2, currency.getNameRU());
                preparedStatement.setString(3, currency.getNameEN());
                preparedStatement.setLong(4, currency.getId());
                result = preparedStatement.executeUpdate();
            }
        } catch (SQLIntegrityConstraintViolationException e) {
            throw new DaoConstraintViolationException("Duplicate data currency", e);
        } catch (SQLException e) {
            logger.log(Level.ERROR, "Database exception during update currency", e);
            throw new DaoException("Database exception during update currency ", e);
        }
        return 1 == result;
    }

    @Override
    public boolean delete(Currency currency) throws DaoException {
        int result;
        try {
            try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_DELETE_CURRENCY)) {
                preparedStatement.setLong(1, currency.getId());
                result = preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            logger.log(Level.ERROR, "Database exception during delete currency", e);
            throw new DaoException("Database exception during delete currency ", e);
        }
        return 1 == result;
    }


    @Override
    public int countRows() throws DaoException {
        int count;
        try {
            try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_SELECT_COUNT_CURRENCIES)) {
                ResultSet resultSet = preparedStatement.executeQuery();
                resultSet.next();
                count = resultSet.getInt(1);
            }
        } catch (SQLException e) {
            logger.log(Level.ERROR, "Database exception during fiend count currency row", e);
            throw new DaoException("Database exception during fiend count currency row", e);
        }
        return count;
    }

    @Override
    public List<Currency> findAll() throws DaoException {
        List<Currency> currencies = new ArrayList<>();
        try {
            try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_SELECT_ALL_CURRENCIES)) {
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    Long id = resultSet.getLong(ColumnName.CURRENCY_ID);
                    String iso = resultSet.getString(ColumnName.CURRENCY_ISO);
                    String nameRU = resultSet.getString(ColumnName.CURRENCY_NAME_RU);
                    String nameEN = resultSet.getString(ColumnName.CURRENCY_NAME_EN);
                    Currency currency = new Currency.Builder()
                            .addId(id)
                            .addIso(iso)
                            .addNameRU(nameRU)
                            .addNameEN(nameEN)
                            .build();
                    currencies.add(currency);
                }
            }
        } catch (SQLException e) {
            logger.log(Level.ERROR, "Database exception during fiend all currency", e);
            throw new DaoException("Database exception during fiend all currency", e);
        }
        return currencies;
    }

    @Override
    public List findAllSKV() throws DaoException {
        List<Currency> currencies = new ArrayList<>();
        try {
            try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_SELECT_ALL_SKV_CURRENCIES)) {
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    Long id = resultSet.getLong(ColumnName.CURRENCY_ID);
                    String iso = resultSet.getString(ColumnName.CURRENCY_ISO);
                    String nameRU = resultSet.getString(ColumnName.CURRENCY_NAME_RU);
                    String nameEN = resultSet.getString(ColumnName.CURRENCY_NAME_EN);
                    Currency currency = new Currency.Builder()
                            .addId(id)
                            .addIso(iso)
                            .addNameRU(nameRU)
                            .addNameEN(nameEN)
                            .build();
                    currencies.add(currency);
                }
            }
        } catch (SQLException e) {
            logger.log(Level.ERROR, "Database exception during fiend all SKV currency", e);
            throw new DaoException("Database exception during fiend all SKV currency", e);
        }
        return currencies;
    }
}
