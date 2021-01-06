package com.epam.esm.model.dao.impl;

import com.epam.esm.entity.User;
import com.epam.esm.exception.DaoConstraintViolationException;
import com.epam.esm.exception.DaoException;
import com.epam.esm.model.dao.AbstractDao;
import com.epam.esm.model.dao.UserDao;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDaoImpl extends AbstractDao implements UserDao {

    private static final Logger logger = LogManager.getLogger(UserDaoImpl.class);

    private static final String SQL_SELECT_ALL_USERS_PAGE = "SELECT id, login, password, fullname, role FROM user  ORDER BY login LIMIT ? Offset ? ";
    private static final String SQL_SELECT_ALL_USERS = "SELECT id, login, password, fullname, role FROM user ";
    private static final String SQL_SELECT_USER_BY_ID = "SELECT id, login, password, fullname, role FROM user WHERE id= ? ";
    private static final String SQL_SELECT_PASSWORD_BY_LOGIN = "SELECT password FROM user WHERE login= ? ";
    private static final String SQL_SELECT_USER_BY_LOGIN = "SELECT id, login, password, fullname, role FROM user WHERE login= ? ";
    private static final String SQL_INSERT_USER = "INSERT INTO user(login, fullname, role) VALUES (?, ?, ?)";
    private static final String SQL_INSERT_USER_WITH_PASSWORD = "INSERT INTO user(login, password, fullname, role) VALUES (?, ?,?, ?)";
    private static final String SQL_UPDATE_USER = "UPDATE user SET login=?, fullname = ?, role=? WHERE id= ?";
    private static final String SQL_DELETE_USER = "DELETE FROM user WHERE id=?";
    private static final String SQL_SELECT_COUNT_USERS = "SELECT COUNT(login) FROM user";

//    @Override
//    public List<User> findAll(int limit, int startPosition) throws DaoException {
//        List<User> users = new ArrayList<>();
//        try {
//            try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_SELECT_ALL_USERS_PAGE)) {
//                preparedStatement.setLong(1, limit);
//                preparedStatement.setLong(2, startPosition);
//                ResultSet resultSet = preparedStatement.executeQuery();
//                while (resultSet.next()) {
//                    Long id = resultSet.getLong(ColumnName.USER_ID);
//                    String login = resultSet.getString(ColumnName.USER_LOGIN);
//                    String fullName = resultSet.getString(ColumnName.USER_FULL_NAME);
//                    String role = resultSet.getString(ColumnName.USER_ROLE);
//                    User user = new User.Builder()
//                            .addId(id)
//                            .addLogin(login)
//                            .addFullName(fullName)
//                            .addRole(role)
//                            .build();
//                    users.add(user);
//                }
//            }
//        } catch (SQLException e) {
//            logger.log(Level.ERROR, "Database exception during fiend all user", e);
//            throw new DaoException("Database exception during fiend all user", e);
//        }
//        return users;
//    }

    @Override
    public User findById(Long id) throws DaoException {
        User user = null;
        try {
            try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_SELECT_USER_BY_ID)) {
                preparedStatement.setLong(1, id);
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    String login = resultSet.getString(ColumnName.USER_LOGIN);
                    String fullName = resultSet.getString(ColumnName.USER_FULL_NAME);
                    String role = resultSet.getString(ColumnName.USER_ROLE);
                    user = new User.Builder()
                            .addId(id)
                            .addLogin(login)
                            .addFullName(fullName)
                            .addRole(role)
                            .build();
                }
            }
        } catch (SQLException e) {
            logger.log(Level.ERROR, "Database exception during fiend user by id", e);
            throw new DaoException("Database exception during fiend user by id", e);
        }
        return user;
    }

    @Override
    public Long create(User user) throws DaoException, DaoConstraintViolationException {
        int result;
        try {
            try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_INSERT_USER, Statement.RETURN_GENERATED_KEYS)) {
                preparedStatement.setString(1, user.getLogin());
                preparedStatement.setString(2, user.getFullName());
                preparedStatement.setString(3, user.getRole().getValue());
                result = preparedStatement.executeUpdate();
                if (1 == result) {
                    ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
                    if (generatedKeys.next()) {
                        return generatedKeys.getLong(1);
                    }
                }
            }
        } catch (SQLIntegrityConstraintViolationException e) {
            throw new DaoConstraintViolationException("Duplicate data user", e);
        } catch (SQLException e) {
            logger.log(Level.ERROR, "Database exception during createCheckEn user", e);
            throw new DaoException("Database exception during createCheckEn user", e);
        }
        return 0L;
    }

    @Override
    public Long create(User user, String password) throws DaoException, DaoConstraintViolationException {
        int result;
        try {
            try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_INSERT_USER_WITH_PASSWORD, Statement.RETURN_GENERATED_KEYS)) {
                preparedStatement.setString(1, user.getLogin());
                preparedStatement.setString(2, password);
                preparedStatement.setString(3, user.getFullName());
                preparedStatement.setString(4, user.getRole().getValue());
                result = preparedStatement.executeUpdate();
                if (1 == result) {
                    ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
                    if (generatedKeys.next()) {
                        return generatedKeys.getLong(1);
                    }
                }
            }
        } catch (SQLIntegrityConstraintViolationException e) {
            throw new DaoConstraintViolationException("Duplicate data user", e);
        } catch (SQLException e) {
            logger.log(Level.ERROR, "Database exception during createCheckEn user", e);
            throw new DaoException("Database exception during createCheckEn user", e);
        }
        return 0L;
    }

    @Override
    public String findPasswordByLogin(String login) throws DaoException {
        String password;
        try {
            try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_SELECT_PASSWORD_BY_LOGIN)) {
                preparedStatement.setString(1, login);
                ResultSet resultSet = preparedStatement.executeQuery();
                resultSet.next();
                    password = resultSet.getString(ColumnName.USER_PASSWORD);
            }
        } catch (SQLException e) {
            logger.log(Level.ERROR, "Database exception during find password by login", e);
            throw new DaoException("Database exception during fiend password by login", e);
        }
        return password;
    }

    @Override
    public boolean update(User user) throws DaoException, DaoConstraintViolationException {
        int result;
        try {
            try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_UPDATE_USER)) {
                preparedStatement.setString(1, user.getLogin());
                preparedStatement.setString(2, user.getFullName());
                preparedStatement.setString(3, user.getRole().getValue());
                preparedStatement.setLong(4, user.getId());
                result = preparedStatement.executeUpdate();
            }
        } catch (SQLIntegrityConstraintViolationException e) {
            throw new DaoConstraintViolationException("Duplicate data user", e);
        } catch (SQLException e) {
            logger.log(Level.ERROR, "Database exception during update user", e);
            throw new DaoException("Database exception during update user ", e);
        }
        return 1 == result;
    }

    @Override
    public boolean delete(User user) throws DaoException {
        int result;
        try {
            try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_DELETE_USER)) {
                preparedStatement.setLong(1, user.getId());
                result = preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            logger.log(Level.ERROR, "Database exception during delete user", e);
            throw new DaoException("Database exception during delete user ", e);
        }
        return 1 == result;
    }

    @Override
    public User findByLogin(String login) throws DaoException {
        User user = null;
        try {
            try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_SELECT_USER_BY_LOGIN)) {
                preparedStatement.setString(1, login);
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    Long id = resultSet.getLong(ColumnName.USER_ID);
                    String fullName = resultSet.getString(ColumnName.USER_FULL_NAME);
                    String role = resultSet.getString(ColumnName.USER_ROLE);
                    user = new User.Builder()
                            .addId(id)
                            .addLogin(login)
                            .addFullName(fullName)
                            .addRole(role)
                            .build();
                }
            }
        } catch (SQLException e) {
            logger.log(Level.ERROR, "Database exception during fiend user by login", e);
            throw new DaoException("Database exception during fiend user by login", e);
        }
        return user;
    }

//    @Override
//    public int countRows() throws DaoException {
//        int count;
//        try {
//            try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_SELECT_COUNT_USERS)) {
//                ResultSet resultSet = preparedStatement.executeQuery();
//                resultSet.next();
//                count = resultSet.getInt(1);
//            }
//        } catch (SQLException e) {
//            logger.log(Level.ERROR, "Database exception during fiend count users row", e);
//            throw new DaoException("Database exception during fiend count users row", e);
//        }
//        return count;
//    }

    @Override
    public List<User> findAll() throws DaoException {
        List<User> users = new ArrayList<>();
        try {
            try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_SELECT_ALL_USERS)) {
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    Long id = resultSet.getLong(ColumnName.USER_ID);
                    String login = resultSet.getString(ColumnName.USER_LOGIN);
                    String fullName = resultSet.getString(ColumnName.USER_FULL_NAME);
                    String role = resultSet.getString(ColumnName.USER_ROLE);
                    User user = new User.Builder()
                            .addId(id)
                            .addLogin(login)
                            .addFullName(fullName)
                            .addRole(role)
                            .build();
                    users.add(user);
                }
            }
        } catch (SQLException e) {
            logger.log(Level.ERROR, "Database exception during fiend all user", e);
            throw new DaoException("Database exception during fiend all user", e);
        }
        return users;
    }
}
