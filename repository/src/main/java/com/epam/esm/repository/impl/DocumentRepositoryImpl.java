package com.epam.esm.repository.impl;


import com.epam.esm.repository.DocumentRepository;
import com.epam.esm.connection.ConnectionHandler;
import com.epam.esm.exception.DatabaseException;
import com.epam.esm.model.Document;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


@Repository
public class DocumentRepositoryImpl extends AbstractRepositoryImpl implements DocumentRepository {

    private static final Logger logger = LogManager.getLogger(DocumentRepositoryImpl.class);

    private ConnectionHandler connectionHandler;

    @PostConstruct
    public void postConstruct() {
        logger.info("postConstruct");
        connectionHandler.createDatabaseTables();
    }


    @Override
    public Document add(Document document) {
        String sql = String.format("INSERT INTO `document`(`uniqueNumber`, `description`) VALUES ('%s','%s')",
                document.getUniqueNumber(), document.getDescription());

        long documentId = executeCreate(sql);
        if (documentId > 0) {
            document.setId(documentId);
            return document;
        } else {
            return null;
        }
    }

    @Override
    public Document getDocumentById(Long id) {
        List<Document> documents = null;
        documents = getAll(" WHERE id=" + id);
        return documents.size() == 0 ? null : documents.get(0);
    }


    @Override
    public void delete(Long id) {
        String sql = String.format("DELETE FROM `document` WHERE `id`='%s'", id);
        executeUpdate(sql);
    }


    public List<Document> getAll() {
        return getAll("");
    }

    public List<Document> getAll(String where) {
        List<Document> result = new ArrayList<>();
        try (Connection connection = connectionHandler.getConnection()) {
            try (Statement statement = connection.createStatement()) {
                String sql = String.format(
                        "SELECT * FROM `document` " + where);
                ResultSet resultSet = statement.executeQuery(sql);
                while (resultSet.next()) {
                    Document document = new Document();
                    document.setId(resultSet.getLong("id"));
                    document.setUniqueNumber(resultSet.getString("uniqueNumber"));
                    document.setDescription(resultSet.getString("description"));
                    result.add(document);
                }
            } catch (SQLException e) {
                logger.error(e.getMessage(), e);
                throw new DatabaseException("Database exception during deleting All document", e);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new DatabaseException("Database exception during connection", e);
        }
        return result;
    }

    @Override
    public ConnectionHandler getConnectionHandler() {
        return connectionHandler;
    }

    @Autowired
    public void setConnectionHandler(ConnectionHandler connectionHandler) {
        this.connectionHandler = connectionHandler;
    }

}
