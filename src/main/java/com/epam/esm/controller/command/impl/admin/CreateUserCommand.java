package com.epam.esm.controller.command.impl.admin;

import com.epam.esm.controller.command.Command;
import com.epam.esm.controller.command.ActionType;
import com.epam.esm.util.ControllerDataUtil;
import com.epam.esm.entity.User;
import com.epam.esm.exception.ServiceException;
import com.epam.esm.exception.CommandException;
import com.epam.esm.model.service.UserService;
import com.epam.esm.model.service.impl.UserServiceImpl;
import com.epam.esm.controller.command.AttributeName;
import com.epam.esm.util.RegexPattern;
import com.epam.esm.util.UserExtractor;
import com.epam.esm.util.DataValidation;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * The action is "Create user".
 */
public class CreateUserCommand implements Command {
    private String directoryPath;
    private final UserService userService = new UserServiceImpl();
    private static final Logger logger = LogManager.getLogger(CreateUserCommand.class);
    private static final String LOGIN = "login";
    private static final String FULL_NAME = "fullname";
    private static final String PASSWORD = "password";
    private static final String REITERATE_PASSWORD = "reiteratepassword";
    private static final String ROLE = "role";

    /**
     * Constructor
     *
     * @param directoryPath - path
     */
    public CreateUserCommand(String directoryPath) {
        this.directoryPath = directoryPath;
    }

    @Override
    public String getDirectoryPath() {
        return directoryPath;
    }

    @Override
    public ActionType execute(HttpServletRequest request, HttpServletResponse response) throws CommandException {
        ControllerDataUtil.removeAttributeMessage(request);
        ControllerDataUtil.removeAttributeError(request);
        final HttpSession session = request.getSession(false);
        try {
            if (DataValidation.isCreateUpdateDeleteOperation(request)) {
                if (DataValidation.isSaveOperation(request)) {
                    UserExtractor userExtractor = new UserExtractor();
                    User createdUser = userExtractor.userNotCheckedFieldsToUser(request);
                    request.setAttribute(AttributeName.USER, createdUser);

                    String login = ControllerDataUtil.getString(request, LOGIN, RegexPattern.ALPHABET_NUMBER_UNDERSCORE_MINUS_VALIDATE_PATTERN);
                    String fullName = ControllerDataUtil.getString(request, FULL_NAME, RegexPattern.ALPHABET_NUMBER_UNDERSCORE_MINUS_BLANK_VALIDATE_PATTERN);
                    String password = ControllerDataUtil.getString(request, PASSWORD, RegexPattern.ALPHABET_NUMBER_UNDERSCORE_MINUS_VALIDATE_PATTERN);
                    String reiteratePassword = ControllerDataUtil.getString(request, REITERATE_PASSWORD, RegexPattern.ALPHABET_NUMBER_UNDERSCORE_MINUS_VALIDATE_PATTERN);
                    String role = ControllerDataUtil.getString(request, ROLE, RegexPattern.ALPHABET_VALIDATE_PATTERN);
                    User newUser = new User.Builder()
                            .addLogin(login)
                            .addFullName(fullName)
                            .addRole(role)
                            .build();
                    if (DataValidation.isUserLengthFieldsValid(request) && password.equals(reiteratePassword)) {
                        userService.create(newUser, password);
                        logger.log(Level.INFO, "Add new user " + newUser.getLogin());
                        session.setAttribute(AttributeName.MESSAGE, "107 " + newUser.getLogin());
                        return ActionType.EDIT_USERS;
                    } else {
                        logger.log(Level.INFO, "incorrect value ");
                        session.setAttribute(AttributeName.ERROR, "102 ");
                        request.setAttribute(AttributeName.USER, newUser);
                        return ActionType.CREATE_USER;
                    }
                }
            }

            final User newUser = new User();
            request.setAttribute(AttributeName.USER, newUser);
            return ActionType.CREATE_USER;
        } catch (ServiceException e) {
            session.setAttribute(AttributeName.ERROR, e);
            logger.log(Level.ERROR, e);
            return ActionType.ERROR;
        }
    }
}
