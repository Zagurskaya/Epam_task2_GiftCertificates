package com.epam.esm.controller.exception;

import com.epam.esm.exception.DaoException;
import com.epam.esm.exception.ServiceException;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

@Controller
public class ExceptionHandlingController {
    Logger log = Logger.getLogger(ExceptionHandlingController.class.getName());

    @ExceptionHandler({SQLException.class, DataAccessException.class})
    public String databaseError() {
        return "databaseError ";
    }

    @ExceptionHandler(ServiceException.class)
    public ModelAndView ServiceExceptionError(HttpServletRequest req, Exception ex) {
        log.log(Level.SEVERE, "Request: " + req.getRequestURL() + " raised " + ex);

        ModelAndView mav = new ModelAndView();
        mav.addObject("errorMessage", ex);
        mav.addObject("url", req.getRequestURL());
        mav.setViewName("error");
        return mav;
    }

    @ExceptionHandler({DaoException.class})
    public ModelAndView DaoExceptionError(HttpServletRequest req, Exception ex) {
        log.log(Level.SEVERE, "Request: " + req.getRequestURL() + " raised " + ex);

        ModelAndView mav = new ModelAndView();
        mav.addObject("errorMessage", ex);
        mav.addObject("url", req.getRequestURL());
        mav.setViewName("error");
        return mav;
    }

    @ExceptionHandler(Exception.class)
    public ModelAndView handleError(HttpServletRequest req, Exception ex) {
        log.log(Level.SEVERE, "Request: " + req.getRequestURL() + " raised " + ex);

        ModelAndView mav = new ModelAndView();
        mav.addObject("errorMessage", ex);
        mav.addObject("url", req.getRequestURL());
        mav.setViewName("error");
        return mav;
    }
}