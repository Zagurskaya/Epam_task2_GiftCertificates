package com.gmail.zagurskaya.repository;

import com.gmail.zagurskaya.model.Document;

public interface DocumentRepository {

    Document add(Document document);

    Document getDocumentById(Long id);

    void delete(Long id);

}
