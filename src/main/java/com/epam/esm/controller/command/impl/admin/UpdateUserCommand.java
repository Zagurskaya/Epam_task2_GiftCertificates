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
import com.epam.esm.util.DataUtil;
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
 * The action is "Update user".
 */
public class UpdateUserCommand implements Command {
    private String directoryPath;
    private final UserService userService = new UserServiceImpl();
    private static final Logger logger = LogManager.getLogger(UpdateUserCommand.class);

    /**
     * Constructor
     *
     * @param directoryPath - path
     */
    public UpdateUserCommand(String directoryPath) {
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
        final Long id = (Long) session.getAttribute(AttributeName.ID);
        if (id == null) return ActionType.INDEX;

        if (DataValidation.isCreateUpdateDeleteOperation(request)) {
            if (DataValidation.isSaveOperation(request)) {
                UserExtractor userExtractor = new UserExtractor();
                User updatedUser = userExtractor.userNotCheckedFieldsToUser(request);
                request.setAttribute(AttributeName.USER, updatedUser);

                String login = DataUtil.getString(updatedUser.getLogin(), RegexPattern.ALPHABET_NUMBER_UNDERSCORE_MINUS_VALIDATE_PATTERN);
                String fullName = DataUtil.getString(updatedUser.getFullName(), RegexPattern.ALPHABET_NUMBER_UNDERSCORE_MINUS_BLANK_VALIDATE_PATTERN);
                String role = DataUtil.getString(updatedUser.getRole().getValue(), RegexPattern.ALPHABET_VALIDATE_PATTERN);

                if (DataValidation.isUserLengthFieldsValid(request)) {
                    User updateUser = new User.Builder()
                            .addId(id)
                            .addLogin(login)
                            .addFullName(fullName)
                            .addRole(role)
                            .build();
                    try {
                        userService.update(updateUser);
                        logger.log(Level.INFO, "Update user " + updatedUser.getLogin());
                        session.setAttribute(AttributeName.MESSAGE, "105 " + updatedUser.getLogin());
                        return ActionType.EDIT_USERS;
                    } catch (ServiceException e) {
                        session.setAttribute(AttributeName.ERROR, e);
                        logger.log(Level.ERROR, e);
                        return ActionType.ERROR;
                    }
                }
            }
        }

        ActionType returnActionType;
        try {
            User updatedUser = userService.findById(id);
            if (updatedUser != null) {
                request.setAttribute(AttributeName.USER, updatedUser);
                returnActionType = ActionType.UPDATE_USER;
            } else {
                logger.log(Level.ERROR, "null user");
                returnActionType = ActionType.ERROR;
            }
        } catch (ServiceException e) {
            logger.log(Level.ERROR, e);
            returnActionType = ActionType.ERROR;
        }
        return returnActionType;
    }
}
