package com.epam.esm.app;

import com.epam.esm.config.AppConfig;
import com.epam.esm.controller.TagController;
import com.epam.esm.model.TagDTO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;


public class App {

    private static final Logger logger = LogManager.getLogger(App.class);

    public static void main(String[] args) {

        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.register(AppConfig.class);
        context.refresh();

        TagController tagController = context.getBean(TagController.class);

        TagDTO tag1 = new TagDTO();
        tag1.setName("Tag1");

        TagDTO tag2 = new TagDTO();
        tag2.setName("Tag2");

        Long id1 = tagController.create(tag1);
        Long id2 = tagController.create(tag2);

        logger.info(tagController.getTagById(id1));
        logger.info(tagController.getTagById(id2));

        tagController.delete(id1);

    }
}
