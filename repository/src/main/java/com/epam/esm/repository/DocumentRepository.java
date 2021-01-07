package com.epam.esm.repository;

import com.epam.esm.model.Document;

public interface DocumentRepository {

    Document add(Document document);

    Document getDocumentById(Long id);

    void delete(Long id);

}
