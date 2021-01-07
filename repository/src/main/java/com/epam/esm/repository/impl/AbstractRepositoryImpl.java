package com.epam.esm.repository.impl;

import com.epam.esm.connection.ConnectionHandler;
import com.epam.esm.exception.DatabaseException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

abstract class AbstractRepositoryImpl {

    private static final Logger logger = LogManager.getLogger(DocumentRepositoryImpl.class);

    abstract ConnectionHandler getConnectionHandler();

    protected boolean executeUpdate(String sql) {
        try (Connection connection = getConnectionHandler().getConnection()) {
            connection.setAutoCommit(false);
            try (Statement statement = connection.createStatement()) {
                int result = statement.executeUpdate(sql);
                connection.commit();
                return 1 == result;
            } catch (SQLException e) {
                connection.rollback();
                logger.error(e.getMessage(), e);
                throw new DatabaseException("Database exception during update document", e);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new DatabaseException("Database exception during connection", e);
        }
    }

    protected long executeCreate(String sql) {
        try (Connection connection = getConnectionHandler().getConnection()) {
            connection.setAutoCommit(false);
            try (Statement statement = connection.createStatement()) {
                int count = statement.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
                connection.commit();
                if (count == 1) {
                    ResultSet generatedKeys = statement.getGeneratedKeys();
                    if (generatedKeys.next()) {
                        return generatedKeys.getLong(1);
                    }
                }
                return 0;
            } catch (SQLException e) {
                connection.rollback();
                logger.error(e.getMessage(), e);
                throw new DatabaseException("Database exception during insert document", e);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new DatabaseException("Database exception during connection", e);
        }
    }
}

