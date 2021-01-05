package com.epam.esm.model.dao.impl;

import com.epam.esm.entity.UserEntry;
import com.epam.esm.exception.DaoException;
import com.epam.esm.exception.DaoConstraintViolationException;
import com.epam.esm.model.dao.AbstractDao;
import com.epam.esm.model.dao.UserEntryDao;
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

public class UserEntryDaoImpl extends AbstractDao implements UserEntryDao {

    private static final Logger logger = LogManager.getLogger(UserEntryDaoImpl.class);

    private static final String SQL_SELECT_ALL_USER_ENTRY_PAGE = "SELECT id, userOperationId, sprEntryId, currencyId, accountDebit, accountCredit, sum, isSpending, rate FROM `userEntry`  ORDER BY id LIMIT ? Offset ? ";
    private static final String SQL_SELECT_ALL_USER_ENTRY = "SELECT id, userOperationId, sprEntryId, currencyId, accountDebit, accountCredit, sum, isSpending, rate FROM `userEntry` ";
    private static final String SQL_SELECT_ALL_ENTRY_BY_DATA = "SELECT a.id, a.userOperationId, a.sprEntryId, a.currencyId, a.accountDebit, a.accountCredit, a.sum, a.isSpending, a.rate FROM userEntry as a LEFT JOIN userOperation as b ON a.userOperationId = b.id WHERE b.timestamp > ? ";
    private static final String SQL_SELECT_USER_ENTRY_BY_ID = "SELECT id, userOperationId, sprEntryId, currencyId, accountDebit, accountCredit, sum, isSpending, rate FROM `userEntry` WHERE id= ? ";
    private static final String SQL_SELECT_USER_ENTRY_BY_OPERATION_ID = "SELECT id, userOperationId, sprEntryId, currencyId, accountDebit, accountCredit, sum, isSpending, rate FROM `userEntry` WHERE userOperationId = ? ";
    private static final String SQL_INSERT_USER_ENTRY = "INSERT INTO userEntry(userOperationId, sprEntryId, currencyId, accountDebit, accountCredit, sum, isSpending, rate) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String SQL_UPDATE_USER_ENTRY = "UPDATE userEntry SET userOperationId=?, sprEntryId = ?, currencyId = ?, accountDebit = ?, accountCredit = ?, sum = ?, isSpending = ?, rate = ? WHERE id= ?";
    private static final String SQL_DELETE_USER_ENTRY = "DELETE FROM userEntry WHERE id=?";
    private static final String SQL_SELECT_COUNT_USER_ENTRY = "SELECT COUNT(id) FROM `userEntry`";

    @Override
    public List<UserEntry> findAll(int limit, int startPosition) throws DaoException {
        List<UserEntry> sprEntries = new ArrayList<>();
        try {
            try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_SELECT_ALL_USER_ENTRY_PAGE)) {
                preparedStatement.setLong(1, limit);
                preparedStatement.setLong(2, startPosition);
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    Long id = resultSet.getLong(ColumnName.USER_ENTRY_ID);
                    Long userOperationId = resultSet.getLong(ColumnName.USER_ENTRY_USER_OPERATION_ID);
                    Long sprEntryId = resultSet.getLong(ColumnName.USER_ENTRY_SPR_ENTRY_ID);
                    Long currencyId = resultSet.getLong(ColumnName.USER_ENTRY_CURRENCY_ID);
                    String accountDebit = resultSet.getString(ColumnName.USER_ENTRY_ACCOUNT_DEBIT);
                    String accountCredit = resultSet.getString(ColumnName.USER_ENTRY_ACCOUNT_CREDIT);
                    BigDecimal sum = resultSet.getBigDecimal(ColumnName.USER_ENTRY_SUM);
                    boolean isSpending = resultSet.getBoolean(ColumnName.USER_ENTRY_IS_SPENDING);
                    BigDecimal rate = resultSet.getBigDecimal(ColumnName.USER_ENTRY_RATE);
                    UserEntry userEntry = new UserEntry.Builder()
                            .addId(id)
                            .addUserOperationId(userOperationId)
                            .addSprEntryId(sprEntryId)
                            .addCurrencyId(currencyId)
                            .addAccountDebit(accountDebit)
                            .addAccountCredit(accountCredit)
                            .addSum(sum)
                            .addIsSpending(isSpending)
                            .addRate(rate)
                            .build();
                    sprEntries.add(userEntry);
                }
            }
        } catch (SQLException e) {
            logger.log(Level.ERROR, "Database exception during fiend all userEntry", e);
            throw new DaoException("Database exception during fiend all userEntry", e);
        }
        return sprEntries;
    }

    @Override
    public UserEntry findById(Long id) throws DaoException {
        UserEntry userEntry = null;
        try {
            try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_SELECT_USER_ENTRY_BY_ID)) {
                preparedStatement.setLong(1, id);
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    Long userOperationId = resultSet.getLong(ColumnName.USER_ENTRY_USER_OPERATION_ID);
                    Long sprEntryId = resultSet.getLong(ColumnName.USER_ENTRY_SPR_ENTRY_ID);
                    Long currencyId = resultSet.getLong(ColumnName.USER_ENTRY_CURRENCY_ID);
                    String accountDebit = resultSet.getString(ColumnName.USER_ENTRY_ACCOUNT_DEBIT);
                    String accountCredit = resultSet.getString(ColumnName.USER_ENTRY_ACCOUNT_CREDIT);
                    BigDecimal sum = resultSet.getBigDecimal(ColumnName.USER_ENTRY_SUM);
                    boolean isSpending = resultSet.getBoolean(ColumnName.USER_ENTRY_IS_SPENDING);
                    BigDecimal rate = resultSet.getBigDecimal(ColumnName.USER_ENTRY_RATE);
                    userEntry = new UserEntry.Builder()
                            .addId(id)
                            .addUserOperationId(userOperationId)
                            .addSprEntryId(sprEntryId)
                            .addCurrencyId(currencyId)
                            .addAccountDebit(accountDebit)
                            .addAccountCredit(accountCredit)
                            .addSum(sum)
                            .addIsSpending(isSpending)
                            .addRate(rate)
                            .build();
                }
            }
        } catch (SQLException e) {
            logger.log(Level.ERROR, "Database exception during fiend userEntry by id", e);
            throw new DaoException("Database exception during fiend userEntry by id", e);
        }
        return userEntry;
    }

    @Override
    public Long create(UserEntry userEntry) throws DaoException, DaoConstraintViolationException {
        int result;
        try {
            try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_INSERT_USER_ENTRY, Statement.RETURN_GENERATED_KEYS)) {
                preparedStatement.setLong(1, userEntry.getUserOperationId());
                preparedStatement.setLong(2, userEntry.getSprEntryId());
                preparedStatement.setLong(3, userEntry.getCurrencyId());
                preparedStatement.setString(4, userEntry.getAccountDebit());
                preparedStatement.setString(5, userEntry.getAccountCredit());
                preparedStatement.setBigDecimal(6, userEntry.getSum());
                preparedStatement.setBoolean(7, userEntry.getIsSpending());
                preparedStatement.setBigDecimal(8, userEntry.getRate());
                result = preparedStatement.executeUpdate();
                if (1 == result) {
                    ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
                    if (generatedKeys.next()) {
                        return generatedKeys.getLong(1);
                    }
                }
            }
        } catch (SQLIntegrityConstraintViolationException e) {
            throw new DaoConstraintViolationException("Duplicate data userEntry", e);
        } catch (SQLException e) {
            logger.log(Level.ERROR, "Database exception during createCheckEn userEntry", e);
            throw new DaoException("Database exception during createCheckEn userEntry", e);
        }
        return 0L;
    }

    @Override
    public boolean update(UserEntry userEntry) throws DaoException, DaoConstraintViolationException {
        int result;
        try {
            try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_UPDATE_USER_ENTRY)) {
                preparedStatement.setLong(1, userEntry.getId());
                preparedStatement.setLong(2, userEntry.getUserOperationId());
                preparedStatement.setLong(3, userEntry.getSprEntryId());
                preparedStatement.setLong(4, userEntry.getCurrencyId());
                preparedStatement.setString(5, userEntry.getAccountDebit());
                preparedStatement.setString(6, userEntry.getAccountCredit());
                preparedStatement.setBigDecimal(7, userEntry.getSum());
                preparedStatement.setBoolean(8, userEntry.getIsSpending());
                preparedStatement.setBigDecimal(9, userEntry.getRate());
                result = preparedStatement.executeUpdate();
            }
        } catch (SQLIntegrityConstraintViolationException e) {
            throw new DaoConstraintViolationException("Duplicate data userEntry", e);
        } catch (SQLException e) {
            logger.log(Level.ERROR, "Database exception during update userEntry", e);
            throw new DaoException("Database exception during update userEntry ", e);
        }
        return 1 == result;
    }

    @Override
    public boolean delete(UserEntry userEntry) throws DaoException {
        int result;
        try {
            try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_DELETE_USER_ENTRY)) {
                preparedStatement.setLong(1, userEntry.getId());
                result = preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            logger.log(Level.ERROR, "Database exception during delete userEntry", e);
            throw new DaoException("Database exception during delete userEntry ", e);
        }
        return 1 == result;
    }

    @Override
    public int countRows() throws DaoException {
        int count;
        try {
            try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_SELECT_COUNT_USER_ENTRY)) {
                ResultSet resultSet = preparedStatement.executeQuery();
                resultSet.next();
                count = resultSet.getInt(1);
            }
        } catch (SQLException e) {
            logger.log(Level.ERROR, "Database exception during fiend count userEntrys row", e);
            throw new DaoException("Database exception during fiend count userEntrys row", e);
        }
        return count;
    }

    @Override
    public List<UserEntry> findAll() throws DaoException {
        List<UserEntry> sprEntries = new ArrayList<>();
        try {
            try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_SELECT_ALL_USER_ENTRY)) {
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    Long id = resultSet.getLong(ColumnName.USER_ENTRY_ID);
                    Long userOperationId = resultSet.getLong(ColumnName.USER_ENTRY_USER_OPERATION_ID);
                    Long sprEntryId = resultSet.getLong(ColumnName.USER_ENTRY_SPR_ENTRY_ID);
                    Long currencyId = resultSet.getLong(ColumnName.USER_ENTRY_CURRENCY_ID);
                    String accountDebit = resultSet.getString(ColumnName.USER_ENTRY_ACCOUNT_DEBIT);
                    String accountCredit = resultSet.getString(ColumnName.USER_ENTRY_ACCOUNT_CREDIT);
                    BigDecimal sum = resultSet.getBigDecimal(ColumnName.USER_ENTRY_SUM);
                    boolean isSpending = resultSet.getBoolean(ColumnName.USER_ENTRY_IS_SPENDING);
                    BigDecimal rate = resultSet.getBigDecimal(ColumnName.USER_ENTRY_RATE);
                    UserEntry userEntry = new UserEntry.Builder()
                            .addId(id)
                            .addUserOperationId(userOperationId)
                            .addSprEntryId(sprEntryId)
                            .addCurrencyId(currencyId)
                            .addAccountDebit(accountDebit)
                            .addAccountCredit(accountCredit)
                            .addSum(sum)
                            .addIsSpending(isSpending)
                            .addRate(rate)
                            .build();
                    sprEntries.add(userEntry);
                }
            }
        } catch (SQLException e) {
            logger.log(Level.ERROR, "Database exception during fiend all userEntry", e);
            throw new DaoException("Database exception during fiend all userEntry", e);
        }
        return sprEntries;
    }

    @Override
    public List<UserEntry> findUserEntriesByOperationId(Long operationId) throws DaoException {
        List<UserEntry> sprEntries = new ArrayList<>();
        try {
            try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_SELECT_USER_ENTRY_BY_OPERATION_ID)) {
                preparedStatement.setLong(1, operationId);
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    Long id = resultSet.getLong(ColumnName.USER_ENTRY_ID);
                    Long sprEntryId = resultSet.getLong(ColumnName.USER_ENTRY_SPR_ENTRY_ID);
                    Long currencyId = resultSet.getLong(ColumnName.USER_ENTRY_CURRENCY_ID);
                    String accountDebit = resultSet.getString(ColumnName.USER_ENTRY_ACCOUNT_DEBIT);
                    String accountCredit = resultSet.getString(ColumnName.USER_ENTRY_ACCOUNT_CREDIT);
                    BigDecimal sum = resultSet.getBigDecimal(ColumnName.USER_ENTRY_SUM);
                    boolean isSpending = resultSet.getBoolean(ColumnName.USER_ENTRY_IS_SPENDING);
                    BigDecimal rate = resultSet.getBigDecimal(ColumnName.USER_ENTRY_RATE);
                    UserEntry userEntry = new UserEntry.Builder()
                            .addId(id)
                            .addUserOperationId(operationId)
                            .addSprEntryId(sprEntryId)
                            .addCurrencyId(currencyId)
                            .addAccountDebit(accountDebit)
                            .addAccountCredit(accountCredit)
                            .addSum(sum)
                            .addIsSpending(isSpending)
                            .addRate(rate)
                            .build();
                    sprEntries.add(userEntry);
                }
            }
        } catch (SQLException e) {
            logger.log(Level.ERROR, "Database exception during fiend all userEntry By OperationId", e);
            throw new DaoException("Database exception during fiend all userEntry By OperationId", e);
        }
        return sprEntries;
    }

    @Override
    public List<UserEntry> findAllByData(LocalDate date) throws DaoException {
        List<UserEntry> sprEntries = new ArrayList<>();
        try {
            try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_SELECT_ALL_ENTRY_BY_DATA)) {
                preparedStatement.setString(1, date.toString());
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    Long id = resultSet.getLong(ColumnName.USER_ENTRY_ID);
                    Long userOperationId = resultSet.getLong(ColumnName.USER_ENTRY_USER_OPERATION_ID);
                    Long sprEntryId = resultSet.getLong(ColumnName.USER_ENTRY_SPR_ENTRY_ID);
                    Long currencyId = resultSet.getLong(ColumnName.USER_ENTRY_CURRENCY_ID);
                    String accountDebit = resultSet.getString(ColumnName.USER_ENTRY_ACCOUNT_DEBIT);
                    String accountCredit = resultSet.getString(ColumnName.USER_ENTRY_ACCOUNT_CREDIT);
                    BigDecimal sum = resultSet.getBigDecimal(ColumnName.USER_ENTRY_SUM);
                    boolean isSpending = resultSet.getBoolean(ColumnName.USER_ENTRY_IS_SPENDING);
                    BigDecimal rate = resultSet.getBigDecimal(ColumnName.USER_ENTRY_RATE);
                    UserEntry userEntry = new UserEntry.Builder()
                            .addId(id)
                            .addUserOperationId(userOperationId)
                            .addSprEntryId(sprEntryId)
                            .addCurrencyId(currencyId)
                            .addAccountDebit(accountDebit)
                            .addAccountCredit(accountCredit)
                            .addSum(sum)
                            .addIsSpending(isSpending)
                            .addRate(rate)
                            .build();
                    sprEntries.add(userEntry);
                }
            }
        } catch (SQLException e) {
            logger.log(Level.ERROR, "Database exception during fiend all by data", e);
            throw new DaoException("Database exception during fiend all by data", e);
        }
        return sprEntries;
    }
}
