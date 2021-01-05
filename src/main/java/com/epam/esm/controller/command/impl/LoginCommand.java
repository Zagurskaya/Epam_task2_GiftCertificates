package com.epam.esm.controller.command.impl;

import com.epam.esm.controller.command.ActionType;
import com.epam.esm.controller.command.Command;
import com.epam.esm.util.ControllerDataUtil;
import com.epam.esm.entity.User;
import com.epam.esm.exception.ServiceException;
import com.epam.esm.exception.CommandException;
import com.epam.esm.model.service.UserService;
import com.epam.esm.model.service.impl.UserServiceImpl;
import com.epam.esm.controller.command.AttributeName;
import com.epam.esm.util.RegexPattern;
import com.epam.esm.util.DataValidation;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * The action is "Registration".
 */
public class LoginCommand implements Command {
    private String directoryPath;
    private static final Logger logger = LogManager.getLogger(LoginCommand.class);
    private static final String LOGIN = "login";
    private static final String PASSWORD = "password";
    private UserService userService = new UserServiceImpl();

    /**
     * Constructor
     *
     * @param directoryPath - path
     */
    public LoginCommand(String directoryPath) {
        this.directoryPath = directoryPath;
    }

    @Override
    public String getDirectoryPath() {
        return directoryPath;
    }

    @Override
    public ActionType execute(HttpServletRequest request, HttpServletResponse response) throws CommandException {
        final HttpSession session = request.getSession(false);
        try {
            if (DataValidation.isCreateUpdateDeleteOperation(request)) {
                String login = ControllerDataUtil.getString(request, LOGIN, RegexPattern.ALPHABET_NUMBER_UNDERSCORE_MINUS_BLANK_VALIDATE_PATTERN);
                String password = ControllerDataUtil.getString(request, PASSWORD, RegexPattern.ALPHABET_NUMBER_UNDERSCORE_MINUS_BLANK_VALIDATE_PATTERN);
                User user;
                user = userService.findUserByLoginAndValidPassword(login, password);
                if (user != null) {
                    session.setAttribute(AttributeName.USER, user);
                    return ActionType.PROFILE;
                } else {
                    logger.log(Level.ERROR, "Value incorrect");
                    session.setAttribute(AttributeName.ERROR, "102 ");
                    return ActionType.LOGIN;
                }
            }
            return ActionType.LOGIN;
        } catch (ServiceException e) {
            session.setAttribute(AttributeName.ERROR, e);
            logger.log(Level.ERROR, e);
            return ActionType.ERROR;
        }
    }
}
