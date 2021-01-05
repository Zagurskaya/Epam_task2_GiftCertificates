package com.epam.esm.model.dao.impl;

import com.epam.esm.entity.RateNB;
import com.epam.esm.exception.DaoException;
import com.epam.esm.exception.DaoConstraintViolationException;
import com.epam.esm.model.dao.AbstractDao;
import com.epam.esm.model.dao.RateNBDao;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class RateNBDaoImpl extends AbstractDao implements RateNBDao {

    private static final Logger logger = LogManager.getLogger(RateNBDaoImpl.class);

    private static final String SQL_SELECT_ALL_RATENBS = "SELECT id, currencyId, date, sum FROM rateNB  ORDER BY id LIMIT ? Offset ? ";
    private static final String SQL_SELECT_RATENB_BY_ID = "SELECT id, currencyId, date, sum FROM rateNB WHERE id= ? ";
    private static final String SQL_SELECT_RATENB_BY_DATA_AND_RATENB = "SELECT id, currencyId, date, sum FROM rateNB WHERE date<= ? AND currencyId= ?";
    private static final String SQL_INSERT_RATENB = "INSERT INTO rateNB(currencyId, date, sum) VALUES (?, ?, ?)";
    private static final String SQL_UPDATE_RATENB = "UPDATE rateNB SET currencyId=?, date = ?, sum = ? WHERE id= ?";
    private static final String SQL_DELETE_RATENB = "DELETE FROM rateNB WHERE id=?";
    private static final String SQL_SELECT_COUNT_RATENBS = "SELECT COUNT(id) FROM rateNB";

    @Override
    public List<RateNB> findAll(int limit, int startPosition) throws DaoException {
        List<RateNB> rateNBs = new ArrayList<>();
        try {
            try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_SELECT_ALL_RATENBS)) {
                preparedStatement.setLong(1, limit);
                preparedStatement.setLong(2, startPosition);
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    Long id = resultSet.getLong(ColumnName.RATENB_ID);
                    Long currencyId = resultSet.getLong(ColumnName.RATENB_CURRENCY_ID);
                    LocalDate date = resultSet.getObject(ColumnName.RATENB_DATE, LocalDate.class);
                    BigDecimal sum = resultSet.getBigDecimal(ColumnName.RATENB_SUM);
                    RateNB rateNB = new RateNB.Builder()
                            .addId(id)
                            .addCurrencyId(currencyId)
                            .addDate(date)
                            .addSum(sum)
                            .build();
                    rateNBs.add(rateNB);
                }
            }
        } catch (SQLException e) {
            logger.log(Level.ERROR, "Database exception during fiend all rateNB", e);
            throw new DaoException("Database exception during fiend all rateNB", e);
        }
        return rateNBs;
    }

    @Override
    public RateNB findById(Long id) throws DaoException {
        RateNB rateNB = null;
        try {
            try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_SELECT_RATENB_BY_ID)) {
                preparedStatement.setLong(1, id);
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    Long currencyId = resultSet.getLong(ColumnName.RATENB_CURRENCY_ID);
                    LocalDate date = resultSet.getObject(ColumnName.RATENB_DATE, LocalDate.class);
                    BigDecimal sum = resultSet.getBigDecimal(ColumnName.RATENB_SUM);
                    rateNB = new RateNB.Builder()
                            .addId(id)
                            .addCurrencyId(currencyId)
                            .addDate(date)
                            .addSum(sum)
                            .build();
                }
            }
        } catch (SQLException e) {
            logger.log(Level.ERROR, "Database exception during fiend rateNB by id", e);
            throw new DaoException("Database exception during fiend rateNB by id", e);
        }
        return rateNB;
    }

    @Override
    public Long create(RateNB rateNB) throws DaoConstraintViolationException, DaoException {
        int result;
        try {
            try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_INSERT_RATENB, Statement.RETURN_GENERATED_KEYS)) {
                preparedStatement.setLong(1, rateNB.getCurrencyId());
                preparedStatement.setString(2, rateNB.getDate().toString());
                preparedStatement.setBigDecimal(3, rateNB.getSum());
                result = preparedStatement.executeUpdate();
                if (1 == result) {
                    ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
                    if (generatedKeys.next()) {
                        return generatedKeys.getLong(1);
                    }
                }
            }
        } catch (SQLIntegrityConstraintViolationException e) {
            throw new DaoConstraintViolationException("Duplicate data rateNB", e);
        } catch (SQLException e) {
            logger.log(Level.ERROR, "Database exception during createCheckEn rateNB", e);
            throw new DaoException("Database exception during createCheckEn rateNB", e);
        }
        return 0L;
    }

    @Override
    public boolean update(RateNB rateNB) throws DaoConstraintViolationException, DaoException {
        int result;
        try {
            try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_UPDATE_RATENB)) {
                preparedStatement.setLong(1, rateNB.getCurrencyId());
                preparedStatement.setString(2, rateNB.getDate().toString());
                preparedStatement.setBigDecimal(3, rateNB.getSum());
                preparedStatement.setLong(4, rateNB.getId());
                result = preparedStatement.executeUpdate();
            }
        } catch (SQLIntegrityConstraintViolationException e) {
            throw new DaoConstraintViolationException("Duplicate data rateNB", e);
        } catch (SQLException e) {
            logger.log(Level.ERROR, "Database exception during update rateNB", e);
            throw new DaoException("Database exception during update rateNB ", e);
        }
        return 1 == result;
    }

    @Override
    public boolean delete(RateNB rateNB) throws DaoException {
        int result;
        try {
            try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_DELETE_RATENB)) {
                preparedStatement.setLong(1, rateNB.getId());
                result = preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            logger.log(Level.ERROR, "Database exception during delete rateNB", e);
            throw new DaoException("Database exception during delete rateNB ", e);
        }
        return 1 == result;
    }

    @Override
    public int countRows() throws DaoException {
        int count;
        try {
            try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_SELECT_COUNT_RATENBS)) {
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
    public RateNB rateNBToday(LocalDate date, Long currencyId) throws DaoException {
        RateNB rateNB = null;
        try {
            try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_SELECT_RATENB_BY_DATA_AND_RATENB)) {
                preparedStatement.setString(1, date.toString());
                preparedStatement.setLong(2, currencyId);
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    Long id = resultSet.getLong(ColumnName.RATENB_ID);
                    BigDecimal sum = resultSet.getBigDecimal(ColumnName.RATENB_SUM);
                    rateNB = new RateNB.Builder()
                            .addId(id)
                            .addCurrencyId(currencyId)
                            .addDate(date)
                            .addSum(sum)
                            .build();
                }
            }
        } catch (SQLException e) {
            logger.log(Level.ERROR, "Database exception during fiend rateNB by id", e);
            throw new DaoException("Database exception during fiend rateNB by id", e);
        }
        return rateNB;
    }
}
