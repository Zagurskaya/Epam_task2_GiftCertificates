package com.gmail.zagurskaya.service.impl;

import com.gmail.zagurskaya.model.Document;
import com.gmail.zagurskaya.model.DocumentDTO;
import com.gmail.zagurskaya.repository.DocumentRepository;
import com.gmail.zagurskaya.service.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DocumentServiceImpl implements DocumentService {

    private DocumentRepository documentRepository;

    @Autowired
    public void setDocumentRepository(DocumentRepository documentRepository) {
        this.documentRepository = documentRepository;
    }

    @Override
    public DocumentDTO add(DocumentDTO documentDTO) {
        Document document = convertToEntity(documentDTO);
        Document savedDocument = documentRepository.add(document);
        return convertToDTO(savedDocument);
    }

    @Override
    public DocumentDTO getDocumentById(Long id) {
        Document document = documentRepository.getDocumentById(id);
        return convertToDTO(document);
    }

    private Document convertToEntity(DocumentDTO documentDTO) {
        Document document = new Document();
        document.setId(documentDTO.getId());
        document.setDescription(documentDTO.getDescription());
//        document.setUniqueNumber(documentDTO.getUniqueNumber());
        return document;
    }

    private DocumentDTO convertToDTO(Document document) {
        DocumentDTO savedDocumentDTO = new DocumentDTO();
        savedDocumentDTO.setId(document.getId());
        savedDocumentDTO.setDescription(document.getDescription());
        savedDocumentDTO.setUniqueNumber(document.getUniqueNumber());
        return savedDocumentDTO;
    }

    @Override
    public void delete(Long id) {
        documentRepository.delete(id);
    }
}
