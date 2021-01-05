package com.epam.esm.model.dao.impl;

import com.epam.esm.entity.Kassa;
import com.epam.esm.exception.DaoException;
import com.epam.esm.exception.DaoConstraintViolationException;
import com.epam.esm.model.dao.AbstractDao;
import com.epam.esm.model.dao.KassaDao;
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

public class KassaDaoImpl extends AbstractDao implements KassaDao {

    private static final Logger logger = LogManager.getLogger(KassaDaoImpl.class);

    private static final String SQL_SELECT_ALL_KASSA = "SELECT id, currencyId, received, coming, spending, transmitted, balance, userId, date, dutiesId FROM kassa  ORDER BY id LIMIT ? Offset ? ";
    private static final String SQL_SELECT_KASSA_BY_ID = "SELECT id, currencyId, received, coming, spending, transmitted, balance, userId, date, dutiesId FROM kassa WHERE id= ? ";
    private static final String SQL_SELECT_KASSA_BY_CURRENCY_ID_DATE_DUTIES = "SELECT id, currencyId, received, coming, spending, transmitted, balance, userId, date, dutiesId FROM kassa WHERE date = ? AND dutiesId = ? AND currencyId = ? ";
    private static final String SQL_SELECT_KASSA_BY_USER_ID_AND_DUTIES_ID = "SELECT id, currencyId, received, coming, spending, transmitted, balance, userId, date, dutiesId FROM kassa WHERE userId = ? AND dutiesId = ? ";
    private static final String SQL_INSERT_KASSA = "INSERT INTO kassa(currencyId, received, coming, spending, transmitted, balance, userId, date, dutiesId) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String SQL_UPDATE_KASSA = "UPDATE kassa SET currencyId = ?, received = ?, coming = ?,  spending = ?, transmitted = ?, balance = ?, userId = ?, date = ? , dutiesId = ? WHERE id= ?";
    private static final String SQL_DELETE_KASSA = "DELETE FROM kassa WHERE id=?";
    private static final String SQL_SELECT_COUNT_KASSAS = "SELECT COUNT(id) FROM kassa";

    @Override
    public List<Kassa> findAll(int limit, int startPosition) throws DaoException {
        List<Kassa> kassaList = new ArrayList<>();
        try {
            try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_SELECT_ALL_KASSA)) {
                preparedStatement.setLong(1, limit);
                preparedStatement.setLong(2, startPosition);
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    Long id = resultSet.getLong(ColumnName.KASSA_ID);
                    Long currencyId = resultSet.getLong(ColumnName.KASSA_CURRENCY_ID);
                    BigDecimal received = resultSet.getBigDecimal(ColumnName.KASSA_RESEIVED);
                    BigDecimal coming = resultSet.getBigDecimal(ColumnName.KASSA_COMING);
                    BigDecimal spending = resultSet.getBigDecimal(ColumnName.KASSA_SPENDING);
                    BigDecimal transmitted = resultSet.getBigDecimal(ColumnName.KASSA_TRANSMITTED);
                    BigDecimal balance = resultSet.getBigDecimal(ColumnName.KASSA_BALANCE);
                    Long userId = resultSet.getLong(ColumnName.KASSA_USER_ID);
                    LocalDate date = resultSet.getObject(ColumnName.KASSA_DATE, LocalDate.class);
                    Long dutiesId = resultSet.getLong(ColumnName.KASSA_CURRENCY_ID);
                    Kassa kassa = new Kassa.Builder()
                            .addId(id)
                            .addCurrencyId(currencyId)
                            .addReceived(received)
                            .addComing(coming)
                            .addSpending(spending)
                            .addTransmitted(transmitted)
                            .addBalance(balance)
                            .addUserId(userId)
                            .addData(date)
                            .addDutiesId(dutiesId)
                            .build();
                    kassaList.add(kassa);
                }
            }
        } catch (SQLException e) {
            logger.log(Level.ERROR, "Database exception during fiend all kassa", e);
            throw new DaoException("Database exception during fiend all kassa", e);
        }
        return kassaList;
    }

    @Override
    public Kassa findById(Long id) throws DaoException {
        Kassa kassa = null;
        try {
            try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_SELECT_KASSA_BY_ID)) {
                preparedStatement.setLong(1, id);
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    Long currencyId = resultSet.getLong(ColumnName.KASSA_CURRENCY_ID);
                    BigDecimal received = resultSet.getBigDecimal(ColumnName.KASSA_RESEIVED);
                    BigDecimal coming = resultSet.getBigDecimal(ColumnName.KASSA_COMING);
                    BigDecimal spending = resultSet.getBigDecimal(ColumnName.KASSA_SPENDING);
                    BigDecimal transmitted = resultSet.getBigDecimal(ColumnName.KASSA_TRANSMITTED);
                    BigDecimal balance = resultSet.getBigDecimal(ColumnName.KASSA_BALANCE);
                    Long userId = resultSet.getLong(ColumnName.KASSA_USER_ID);
                    LocalDate date = resultSet.getObject(ColumnName.KASSA_DATE, LocalDate.class);
                    Long dutiesId = resultSet.getLong(ColumnName.KASSA_CURRENCY_ID);
                    kassa = new Kassa.Builder()
                            .addId(id)
                            .addCurrencyId(currencyId)
                            .addReceived(received)
                            .addComing(coming)
                            .addSpending(spending)
                            .addTransmitted(transmitted)
                            .addBalance(balance)
                            .addUserId(userId)
                            .addData(date)
                            .addDutiesId(dutiesId)
                            .build();
                }
            }
        } catch (SQLException e) {
            logger.log(Level.ERROR, "Database exception during fiend kassa by id", e);
            throw new DaoException("Database exception during fiend kassa by id", e);
        }
        return kassa;
    }

    @Override
    public Long create(Kassa kassa) throws DaoException, DaoConstraintViolationException {
        int result;
        try {
            try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_INSERT_KASSA, Statement.RETURN_GENERATED_KEYS)) {
                preparedStatement.setLong(1, kassa.getCurrencyId());
                preparedStatement.setBigDecimal(2, kassa.getReceived());
                preparedStatement.setBigDecimal(3, kassa.getComing());
                preparedStatement.setBigDecimal(4, kassa.getSpending());
                preparedStatement.setBigDecimal(5, kassa.getTransmitted());
                preparedStatement.setBigDecimal(6, kassa.getBalance());
                preparedStatement.setLong(7, kassa.getUserId());
                preparedStatement.setString(8, kassa.getDate().toString());
                preparedStatement.setLong(9, kassa.getDutiesId());
                result = preparedStatement.executeUpdate();
                if (1 == result) {
                    ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
                    if (generatedKeys.next()) {
                        return generatedKeys.getLong(1);
                    }
                }
            }
        } catch (SQLIntegrityConstraintViolationException e) {
            throw new DaoConstraintViolationException("Duplicate data kassa", e);
        } catch (SQLException e) {
            logger.log(Level.ERROR, "Database exception during createCheckEn kassa", e);
            throw new DaoException("Database exception during createCheckEn kassa", e);
        }
        return 0L;
    }

    @Override
    public boolean update(Kassa kassa) throws DaoException, DaoConstraintViolationException {
        int result;
        try {
            try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_UPDATE_KASSA)) {
                preparedStatement.setLong(1, kassa.getCurrencyId());
                preparedStatement.setBigDecimal(2, kassa.getReceived());
                preparedStatement.setBigDecimal(3, kassa.getComing());
                preparedStatement.setBigDecimal(4, kassa.getSpending());
                preparedStatement.setBigDecimal(5, kassa.getTransmitted());
                preparedStatement.setBigDecimal(6, kassa.getBalance());
                preparedStatement.setLong(7, kassa.getUserId());
                preparedStatement.setString(8, kassa.getDate().toString());
                preparedStatement.setLong(9, kassa.getDutiesId());
                preparedStatement.setLong(10, kassa.getId());
                result = preparedStatement.executeUpdate();
            }
        } catch (SQLIntegrityConstraintViolationException e) {
            throw new DaoConstraintViolationException("Duplicate data kassa", e);
        } catch (SQLException e) {
            logger.log(Level.ERROR, "Database exception during update kassa", e);
            throw new DaoException("Database exception during update kassa ", e);
        }
        return 1 == result;
    }

    @Override
    public boolean delete(Kassa kassa) throws DaoException {
        int result;
        try {
            try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_DELETE_KASSA)) {
                preparedStatement.setLong(1, kassa.getId());
                result = preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            logger.log(Level.ERROR, "Database exception during delete kassa", e);
            throw new DaoException("Database exception during delete kassa ", e);
        }
        return 1 == result;
    }

    @Override
    public int countRows() throws DaoException {
        int count;
        try {
            try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_SELECT_COUNT_KASSAS)) {
                ResultSet resultSet = preparedStatement.executeQuery();
                resultSet.next();
                count = resultSet.getInt(1);
            }
        } catch (SQLException e) {
            logger.log(Level.ERROR, "Database exception during fiend count kassaList row", e);
            throw new DaoException("Database exception during fiend count kassaList row", e);
        }
        return count;
    }

    @Override
    public Kassa findByCurrencyIdAndDateAndDutiesId(LocalDate date, Long dutiesId, Long currencyId) throws DaoException {
        Kassa kassa = null;
        try {
            try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_SELECT_KASSA_BY_CURRENCY_ID_DATE_DUTIES)) {
                preparedStatement.setString(1, date.toString());
                preparedStatement.setLong(2, dutiesId);
                preparedStatement.setLong(3, currencyId);
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    Long id = resultSet.getLong(ColumnName.KASSA_ID);
                    BigDecimal received = resultSet.getBigDecimal(ColumnName.KASSA_RESEIVED);
                    BigDecimal coming = resultSet.getBigDecimal(ColumnName.KASSA_COMING);
                    BigDecimal spending = resultSet.getBigDecimal(ColumnName.KASSA_SPENDING);
                    BigDecimal transmitted = resultSet.getBigDecimal(ColumnName.KASSA_TRANSMITTED);
                    BigDecimal balance = resultSet.getBigDecimal(ColumnName.KASSA_BALANCE);
                    Long userId = resultSet.getLong(ColumnName.KASSA_USER_ID);
                    kassa = new Kassa.Builder()
                            .addId(id)
                            .addCurrencyId(currencyId)
                            .addReceived(received)
                            .addComing(coming)
                            .addSpending(spending)
                            .addTransmitted(transmitted)
                            .addBalance(balance)
                            .addUserId(userId)
                            .addData(date)
                            .addDutiesId(dutiesId)
                            .build();
                }
            }
        } catch (SQLException e) {
            logger.log(Level.ERROR, "Database exception during findByCurrencyIdAndDateAndDutiesNumber", e);
            throw new DaoException("Database exception during findByCurrencyIdAndDateAndDutiesNumber", e);
        }
        return kassa;
    }

    @Override
    public List<Kassa> findAllByUserIdAndDutiesId(Long userId, Long dutiesId) throws DaoException {
        List<Kassa> kassaList = new ArrayList<>();
        try {
            try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_SELECT_KASSA_BY_USER_ID_AND_DUTIES_ID)) {
                preparedStatement.setLong(1, userId);
                preparedStatement.setLong(2, dutiesId);
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    Long id = resultSet.getLong(ColumnName.KASSA_ID);
                    Long currencyId = resultSet.getLong(ColumnName.KASSA_CURRENCY_ID);
                    BigDecimal received = resultSet.getBigDecimal(ColumnName.KASSA_RESEIVED);
                    BigDecimal coming = resultSet.getBigDecimal(ColumnName.KASSA_COMING);
                    BigDecimal spending = resultSet.getBigDecimal(ColumnName.KASSA_SPENDING);
                    BigDecimal transmitted = resultSet.getBigDecimal(ColumnName.KASSA_TRANSMITTED);
                    BigDecimal balance = resultSet.getBigDecimal(ColumnName.KASSA_BALANCE);
                    LocalDate date = resultSet.getObject(ColumnName.KASSA_DATE, LocalDate.class);
                    Kassa kassa = new Kassa.Builder()
                            .addId(id)
                            .addCurrencyId(currencyId)
                            .addReceived(received)
                            .addComing(coming)
                            .addSpending(spending)
                            .addTransmitted(transmitted)
                            .addBalance(balance)
                            .addUserId(userId)
                            .addData(date)
                            .addDutiesId(dutiesId)
                            .build();
                    kassaList.add(kassa);
                }
            }
        } catch (SQLException e) {
            logger.log(Level.ERROR, "Database exception during find all by userId and dutiesId ", e);
            throw new DaoException("Database exception during find all by userId and dutiesId ", e);
        }
        return kassaList;
    }
}
