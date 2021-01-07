package com.epam.esm.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import javax.annotation.PostConstruct;

import com.epam.esm.exception.DatabaseException;
import com.epam.esm.properties.DatabaseProperties;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ConnectionHandler {

    private final Logger logger = LogManager.getLogger(ConnectionHandler.class);

    private final String ERROR_MESSAGE = "Connection Failed! Check output console.";
    private final DatabaseProperties databaseProperties;

    @Autowired
    public ConnectionHandler(DatabaseProperties databaseProperties) {
        try {
            Class.forName(databaseProperties.getDriver());
        } catch (ClassNotFoundException e) {
            logger.error(e.getMessage(), e);
        }
        this.databaseProperties = databaseProperties;
    }

    public Connection getConnection() {
        try {
            Properties properties = new Properties();
            properties.setProperty("user", databaseProperties.getUsername());
            properties.setProperty("password", databaseProperties.getPassword());
            properties.setProperty("useUnicode", "true");
            properties.setProperty("characterEncoding", "cp1251");
            return DriverManager.getConnection(databaseProperties.getUrl(), properties);
        } catch (SQLException e) {
            logger.error(ERROR_MESSAGE, e);
            throw new DatabaseException(ERROR_MESSAGE);
        }
    }

    @PostConstruct
    public void createDatabaseTables() {
//        String createTableQuery = "CREATE TABLE IF NOT EXISTS PERMISSION(id int auto_increment primary key, name varchar(100))";
        String createTableQuery = "CREATE TABLE IF NOT EXISTS `jd2_project_week2`.`document` (\n" +
                "  `id` INT(100) NOT NULL AUTO_INCREMENT,\n" +
                "  `uniqueNumber` VARCHAR(100) NOT NULL,\n" +
                "  `description` VARCHAR(100) NULL,\n" +
                "  PRIMARY KEY (`id`))";
        try (Connection connection = getConnection()) {
            connection.setAutoCommit(false);
            try (Statement statement = connection.createStatement()) {
                statement.execute(createTableQuery);
                connection.commit();
            } catch (Exception e) {
                connection.rollback();
            }
        } catch (SQLException e) {
            logger.error(ERROR_MESSAGE, e);
            throw new DatabaseException(ERROR_MESSAGE);
        }
    }
}
