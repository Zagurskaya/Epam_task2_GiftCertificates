package com.epam.esm.model.dao.impl;

import com.epam.esm.entity.SprEntry;
import com.epam.esm.exception.DaoException;
import com.epam.esm.exception.DaoConstraintViolationException;
import com.epam.esm.model.dao.AbstractDao;
import com.epam.esm.model.dao.SprEntryDao;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class SprEntryDaoImpl extends AbstractDao implements SprEntryDao {

    private static final Logger logger = LogManager.getLogger(SprEntryDaoImpl.class);

    private static final String SQL_SELECT_ALL_SPR_ENTRY_PAGE = "SELECT id, name, currencyId, sprOperationId, accountDebit, accountCredit, isSpending, rate FROM sprEntry  ORDER BY id LIMIT ? Offset ? ";
    private static final String SQL_SELECT_ALL_SPR_ENTRY = "SELECT id, name, currencyId, sprOperationId, accountDebit, accountCredit, isSpending, rate FROM sprEntry ";
    private static final String SQL_SELECT_ALL_SPR_ENTRY_BY_SPR_OPERATION_ID_AND_CURRENCY_ID = "SELECT id, name, currencyId, sprOperationId, accountDebit, accountCredit, isSpending, rate FROM sprEntry WHERE sprOperationId= ? AND currencyId= ?";
    private static final String SQL_SELECT_SPR_ENTRY_BY_ID = "SELECT id, name, currencyId, sprOperationId, accountDebit, accountCredit, isSpending, rate FROM sprEntry WHERE id= ? ";
    private static final String SQL_INSERT_SPR_ENTRY = "INSERT INTO sprEntry(name, currencyId, sprOperationId, accountDebit, accountCredit, isSpending, rate) VALUES (?, ?, ?, ?, ?, ?, ?)";
    private static final String SQL_UPDATE_SPR_ENTRY = "UPDATE sprEntry SET name=?, currencyId = ?, sprOperationId = ?, accountDebit = ?, accountCredit = ?, isSpending = ?, rate = ? WHERE id= ?";
    private static final String SQL_DELETE_SPR_ENTRY = "DELETE FROM sprEntry WHERE id=?";
    private static final String SQL_SELECT_COUNT_SPR_ENTRY = "SELECT COUNT(id) FROM sprEntry";

    @Override
    public List<SprEntry> findAll(int limit, int startPosition) throws DaoException {
        List<SprEntry> sprEntries = new ArrayList<>();
        try {
            try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_SELECT_ALL_SPR_ENTRY_PAGE)) {
                preparedStatement.setLong(1, limit);
                preparedStatement.setLong(2, startPosition);
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    Long id = resultSet.getLong(ColumnName.SPR_ENTRY_ID);
                    String name = resultSet.getString(ColumnName.SPR_ENTRY_NAME);
                    Long currencyId = resultSet.getLong(ColumnName.SPR_ENTRY_CURRENCY_ID);
                    Long sprOperationId = resultSet.getLong(ColumnName.SPR_ENTRY_SPR_OPERATION_ID);
                    String accountDebit = resultSet.getString(ColumnName.SPR_ENTRY_ACCOUNT_DEBIT);
                    String accountCredit = resultSet.getString(ColumnName.SPR_ENTRY_ACCOUNT_CREDIT);
                    boolean isSpending = resultSet.getBoolean(ColumnName.SPR_ENTRY_IS_SPENDING);
                    BigDecimal rate = resultSet.getBigDecimal(ColumnName.SPR_ENTRY_RATE);
                    SprEntry sprEntry = new SprEntry.Builder()
                            .addId(id)
                            .addName(name)
                            .addCurrencyId(currencyId)
                            .addSprOperationId(sprOperationId)
                            .addAccountDebit(accountDebit)
                            .addAccountCredit(accountCredit)
                            .addIsSpending(isSpending)
                            .addRate(rate)
                            .build();
                    sprEntries.add(sprEntry);
                }
            }
        } catch (SQLException e) {
            logger.log(Level.ERROR, "Database exception during fiend all sprEntry", e);
            throw new DaoException("Database exception during fiend all sprEntry", e);
        }
        return sprEntries;
    }

    @Override
    public SprEntry findById(Long id) throws DaoException {
        SprEntry sprEntry = null;
        try {
            try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_SELECT_SPR_ENTRY_BY_ID)) {
                preparedStatement.setLong(1, id);
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    String name = resultSet.getString(ColumnName.SPR_ENTRY_NAME);
                    Long currencyId = resultSet.getLong(ColumnName.SPR_ENTRY_CURRENCY_ID);
                    Long sprOperationId = resultSet.getLong(ColumnName.SPR_ENTRY_SPR_OPERATION_ID);
                    String accountDebit = resultSet.getString(ColumnName.SPR_ENTRY_ACCOUNT_DEBIT);
                    String accountCredit = resultSet.getString(ColumnName.SPR_ENTRY_ACCOUNT_CREDIT);
                    boolean isSpending = resultSet.getBoolean(ColumnName.SPR_ENTRY_IS_SPENDING);
                    BigDecimal rate = resultSet.getBigDecimal(ColumnName.SPR_ENTRY_RATE);
                    sprEntry = new SprEntry.Builder()
                            .addId(id)
                            .addName(name)
                            .addCurrencyId(currencyId)
                            .addSprOperationId(sprOperationId)
                            .addAccountDebit(accountDebit)
                            .addAccountCredit(accountCredit)
                            .addIsSpending(isSpending)
                            .addRate(rate)
                            .build();
                }
            }
        } catch (SQLException e) {
            logger.log(Level.ERROR, "Database exception during fiend sprEntry by id", e);
            throw new DaoException("Database exception during fiend sprEntry by id", e);
        }
        return sprEntry;
    }

    @Override
    public Long create(SprEntry sprEntry) throws DaoException, DaoConstraintViolationException {
        int result;
        try {
            try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_INSERT_SPR_ENTRY, Statement.RETURN_GENERATED_KEYS)) {
                preparedStatement.setLong(1, sprEntry.getId());
                preparedStatement.setString(2, sprEntry.getName());
                preparedStatement.setLong(3, sprEntry.getCurrencyId());
                preparedStatement.setLong(4, sprEntry.getSprOperationId());
                preparedStatement.setString(5, sprEntry.getAccountDebit());
                preparedStatement.setString(6, sprEntry.getAccountCredit());
                preparedStatement.setBoolean(7, sprEntry.getIsSpending());
                preparedStatement.setBigDecimal(8, sprEntry.getRate());
                result = preparedStatement.executeUpdate();
                if (1 == result) {
                    ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
                    if (generatedKeys.next()) {
                        return generatedKeys.getLong(1);
                    }
                }
            }
        } catch (SQLIntegrityConstraintViolationException e) {
            throw new DaoConstraintViolationException("Duplicate data sprEntry", e);
        } catch (SQLException e) {
            logger.log(Level.ERROR, "Database exception during createCheckEn sprEntry", e);
            throw new DaoException("Database exception during createCheckEn sprEntry", e);
        }
        return 0L;
    }

    @Override
    public boolean update(SprEntry sprEntry) throws DaoException, DaoConstraintViolationException {
        int result;
        try {
            try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_UPDATE_SPR_ENTRY)) {
                preparedStatement.setString(1, sprEntry.getName());
                preparedStatement.setLong(2, sprEntry.getCurrencyId());
                preparedStatement.setLong(3, sprEntry.getSprOperationId());
                preparedStatement.setString(4, sprEntry.getAccountDebit());
                preparedStatement.setString(5, sprEntry.getAccountCredit());
                preparedStatement.setBoolean(6, sprEntry.getIsSpending());
                preparedStatement.setBigDecimal(7, sprEntry.getRate());
                preparedStatement.setLong(8, sprEntry.getId());
                result = preparedStatement.executeUpdate();
            }
        } catch (SQLIntegrityConstraintViolationException e) {
            throw new DaoConstraintViolationException("Duplicate data sprEntry", e);
        } catch (SQLException e) {
            logger.log(Level.ERROR, "Database exception during update sprEntry", e);
            throw new DaoException("Database exception during update sprEntry ", e);
        }
        return 1 == result;
    }

    @Override
    public boolean delete(SprEntry sprEntry) throws DaoException {
        int result;
        try {
            try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_DELETE_SPR_ENTRY)) {
                preparedStatement.setLong(1, sprEntry.getId());
                result = preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            logger.log(Level.ERROR, "Database exception during delete sprEntry", e);
            throw new DaoException("Database exception during delete sprEntry ", e);
        }
        return 1 == result;
    }

    @Override
    public int countRows() throws DaoException {
        int count;
        try {
            try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_SELECT_COUNT_SPR_ENTRY)) {
                ResultSet resultSet = preparedStatement.executeQuery();
                resultSet.next();
                count = resultSet.getInt(1);
            }
        } catch (SQLException e) {
            logger.log(Level.ERROR, "Database exception during fiend count sprEntrys row", e);
            throw new DaoException("Database exception during fiend count sprEntrys row", e);
        }
        return count;
    }

    @Override
    public List<SprEntry> findAll() throws DaoException {
        List<SprEntry> sprEntries = new ArrayList<>();
        try {
            try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_SELECT_ALL_SPR_ENTRY)) {
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    Long id = resultSet.getLong(ColumnName.SPR_ENTRY_ID);
                    String name = resultSet.getString(ColumnName.SPR_ENTRY_NAME);
                    Long currencyId = resultSet.getLong(ColumnName.SPR_ENTRY_CURRENCY_ID);
                    Long sprOperationId = resultSet.getLong(ColumnName.SPR_ENTRY_SPR_OPERATION_ID);
                    String accountDebit = resultSet.getString(ColumnName.SPR_ENTRY_ACCOUNT_DEBIT);
                    String accountCredit = resultSet.getString(ColumnName.SPR_ENTRY_ACCOUNT_CREDIT);
                    boolean isSpending = resultSet.getBoolean(ColumnName.SPR_ENTRY_IS_SPENDING);
                    BigDecimal rate = resultSet.getBigDecimal(ColumnName.SPR_ENTRY_RATE);
                    SprEntry sprEntry = new SprEntry.Builder()
                            .addId(id)
                            .addName(name)
                            .addCurrencyId(currencyId)
                            .addSprOperationId(sprOperationId)
                            .addAccountDebit(accountDebit)
                            .addAccountCredit(accountCredit)
                            .addIsSpending(isSpending)
                            .addRate(rate)
                            .build();
                    sprEntries.add(sprEntry);
                }
            }
        } catch (SQLException e) {
            logger.log(Level.ERROR, "Database exception during fiend all sprEntry", e);
            throw new DaoException("Database exception during fiend all sprEntry", e);
        }
        return sprEntries;
    }

    @Override
    public List<SprEntry> findAllBySprOperationIdAndCurrencyId(Long sprOperationId, Long currencyId) throws DaoException {
        List<SprEntry> sprEntries = new ArrayList<>();
        try {
            try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_SELECT_ALL_SPR_ENTRY_BY_SPR_OPERATION_ID_AND_CURRENCY_ID)) {
                preparedStatement.setLong(1, sprOperationId);
                preparedStatement.setLong(2, currencyId);
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    Long id = resultSet.getLong(ColumnName.SPR_ENTRY_ID);
                    String name = resultSet.getString(ColumnName.SPR_ENTRY_NAME);
                    String accountDebit = resultSet.getString(ColumnName.SPR_ENTRY_ACCOUNT_DEBIT);
                    String accountCredit = resultSet.getString(ColumnName.SPR_ENTRY_ACCOUNT_CREDIT);
                    boolean isSpending = resultSet.getBoolean(ColumnName.SPR_ENTRY_IS_SPENDING);
                    BigDecimal rate = resultSet.getBigDecimal(ColumnName.SPR_ENTRY_RATE);
                    SprEntry sprEntry = new SprEntry.Builder()
                            .addId(id)
                            .addName(name)
                            .addCurrencyId(currencyId)
                            .addSprOperationId(sprOperationId)
                            .addAccountDebit(accountDebit)
                            .addAccountCredit(accountCredit)
                            .addIsSpending(isSpending)
                            .addRate(rate)
                            .build();
                    sprEntries.add(sprEntry);
                }
            }
        } catch (SQLException e) {
            logger.log(Level.ERROR, "Database exception during fiend all By SprOperationId And CurrencyId", e);
            throw new DaoException("Database exception during fiend all By SprOperationId And CurrencyId", e);
        }
        return sprEntries;
    }
}
