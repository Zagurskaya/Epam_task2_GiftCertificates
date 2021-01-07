package com.epam.esm.controller;

import com.epam.esm.model.DocumentDTO;

public interface DocumentController {

    DocumentDTO add(DocumentDTO document);

    DocumentDTO getDocumentById(Long id);

    void delete(Long id);
}
