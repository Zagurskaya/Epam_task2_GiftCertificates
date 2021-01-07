package com.gmail.zagurskaya.service;

import com.gmail.zagurskaya.model.DocumentDTO;

public interface DocumentService {

    DocumentDTO add(DocumentDTO document);

    DocumentDTO getDocumentById(Long id);

    void delete(Long id);
}
