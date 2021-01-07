package com.gmail.zagurskaya.app;

import com.gmail.zagurskaya.config.AppConfig;
import com.gmail.zagurskaya.controller.DocumentController;
import com.gmail.zagurskaya.model.DocumentDTO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;


public class App {

    private static final Logger logger = LogManager.getLogger(App.class);

    public static void main(String[] args) {

        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.register(AppConfig.class);
        context.refresh();

        DocumentController documentController = context.getBean(DocumentController.class);

        DocumentDTO document1 = new DocumentDTO();
        document1.setDescription("Document1");

        DocumentDTO document2 = new DocumentDTO();
        document2.setDescription("Document2");

        DocumentDTO savedDocument1 = documentController.add(document1);
        DocumentDTO savedDocument2 = documentController.add(document2);

        logger.info(documentController.getDocumentById(savedDocument1.getId()));
        logger.info(documentController.getDocumentById(savedDocument2.getId()));

        documentController.delete(savedDocument1.getId());


    }
}
