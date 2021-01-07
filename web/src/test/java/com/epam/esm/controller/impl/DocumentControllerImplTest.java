package com.epam.esm.controller.impl;

import com.epam.esm.controller.DocumentController;
import com.epam.esm.model.DocumentDTO;
import com.epam.esm.service.DocumentService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DocumentControllerImplTest {

    @Mock
    private DocumentService documentService;

    private DocumentController documentController;

    @Before
    public void init() {
        DocumentControllerImpl impl = new DocumentControllerImpl();
        impl.setDocumentService(documentService);
        this.documentController = impl;
    }

    @Test
    public void add() {
        DocumentDTO documentDTO = new DocumentDTO();
        documentDTO.setDescription("1");

        DocumentDTO savedDocumentDTO = new DocumentDTO();
        savedDocumentDTO.setId(1L);
        savedDocumentDTO.setUniqueNumber("000-000-000");
        savedDocumentDTO.setDescription("1");

        when(documentService.add(documentDTO)).thenReturn(savedDocumentDTO);

        DocumentDTO result = documentController.add(documentDTO);
        Assert.assertEquals(savedDocumentDTO, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void addDocumentDescriptionNull() {
        DocumentDTO documentDTO = new DocumentDTO();
        DocumentDTO result = documentController.add(documentDTO);
    }

    @Test(expected = IllegalArgumentException.class)
    public void addDocumentDescriptionEmpty() {
        DocumentDTO documentDTO = new DocumentDTO();
        documentDTO.setDescription("");
        DocumentDTO result = documentController.add(documentDTO);
    }

    @Test(expected = IllegalArgumentException.class)
    public void addDocumentDescriptionLengthMore100() {
        DocumentDTO documentDTO = new DocumentDTO();
        documentDTO.setDescription(String.format("%1$101s", ""));
        DocumentDTO result = documentController.add(documentDTO);
    }

    @Test
    public void addDocumentDescriptionLength100() {
        DocumentDTO documentDTO = new DocumentDTO();
        documentDTO.setDescription(String.format("%1$100s", ""));
        DocumentDTO result = documentController.add(documentDTO);
    }
}