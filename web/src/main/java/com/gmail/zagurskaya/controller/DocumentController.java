package com.gmail.zagurskaya.controller;

import com.gmail.zagurskaya.model.DocumentDTO;

public interface DocumentController {

    DocumentDTO add(DocumentDTO document);

    DocumentDTO getDocumentById(Long id);

    void delete(Long id);
}
