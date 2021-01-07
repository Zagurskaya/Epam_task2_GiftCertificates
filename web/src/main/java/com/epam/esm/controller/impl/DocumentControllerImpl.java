package com.epam.esm.controller.impl;

import com.epam.esm.controller.DocumentController;
import com.epam.esm.model.DocumentDTO;
import com.epam.esm.service.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;

@Controller
public class DocumentControllerImpl implements DocumentController {

    private DocumentService documentService;

    @Autowired
    public void setDocumentService(DocumentService documentService) {
        this.documentService = documentService;
    }

    @Override
    public DocumentDTO add(DocumentDTO document) {
        Assert.hasLength(document.getDescription(), "description is mandatory field");
        Assert.isTrue(document.getDescription().length() <= 100 , "description max length 100");

        return documentService.add(document);
    }

    @Override
    public DocumentDTO getDocumentById(Long id) {
        Assert.notNull(id, "id is mandatory field");
        Assert.isTrue(id >= 0 , "id less than zero");

        return documentService.getDocumentById(id);
    }

    @Override
    public void delete(Long id) {
        Assert.notNull(id, "id is mandatory field");
        Assert.isTrue(id >= 0 , "id less than zero");

        documentService.delete(id);
    }
}
