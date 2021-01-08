package com.epam.esm.app;

import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

public class ApplicationInitializer implements WebApplicationInitializer {

    //Called first when the application starts loading.
    public void onStartup(ServletContext servletContext) throws ServletException {
        System.out.println("Inside application initializer...");

        //Registering the class that incorporates the annotated DispatcherServlet configuration of spring
        AnnotationConfigWebApplicationContext rootContext = new AnnotationConfigWebApplicationContext();
        rootContext.scan("com.epam.esm");

        //Adding the listener for the rootContext
        servletContext.addListener(new ContextLoaderListener(rootContext));

        //Registering the dispatcher servlet mappings.
        ServletRegistration.Dynamic dispatcher = servletContext.addServlet("dispatcher", new DispatcherServlet(rootContext));
        dispatcher.setLoadOnStartup(1);
        dispatcher.addMapping("/");
    }

}