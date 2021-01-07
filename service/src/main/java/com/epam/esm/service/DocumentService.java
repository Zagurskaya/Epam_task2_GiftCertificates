package com.epam.esm.service;

import com.epam.esm.model.DocumentDTO;

public interface DocumentService {

    DocumentDTO add(DocumentDTO document);

    DocumentDTO getDocumentById(Long id);

    void delete(Long id);
}
