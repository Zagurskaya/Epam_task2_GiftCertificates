package com.epam.esm.controller.command;

import com.epam.esm.controller.command.impl.admin.AdminCommand;
import com.epam.esm.controller.command.impl.admin.CreateUserCommand;
import com.epam.esm.controller.command.impl.admin.EditUsersCommand;
import com.epam.esm.controller.command.impl.admin.UpdateUserCommand;
import com.epam.esm.controller.command.impl.ErrorCommand;
import com.epam.esm.controller.command.impl.IndexCommand;
import com.epam.esm.controller.command.impl.LocaleCommand;
import com.epam.esm.controller.command.impl.LoginCommand;
import com.epam.esm.controller.command.impl.LogoutCommand;
import com.epam.esm.controller.command.impl.ProfileCommand;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Action list
 */
public enum ActionType {
//  root
    /**
     * Start page
     */
    INDEX(new IndexCommand(PathPage.PATH_INDEX)),
    /**
     * Error
     */
    ERROR(new ErrorCommand(PathPage.PATH_ERROR)),
    /**
     * Registration
     */
    LOGIN(new LoginCommand(PathPage.PATH_ROOT)),
    /**
     * End of session
     */
    LOGOUT(new LogoutCommand(PathPage.PATH_ROOT)),
    /**
     * Change locale
     */
    LOCALE(new LocaleCommand(PathPage.PATH_ROOT)),
    /**
     * Profile
     */
    PROFILE(new ProfileCommand(PathPage.PATH_ROOT)),
//---------------ADMIN---------------------------------
    /**
     * Admin home page
     */
    ADMIN(new AdminCommand(PathPage.PATH_ADMIN)),
    /**
     * View and delete users
     */
    EDIT_USERS(new EditUsersCommand(PathPage.PATH_ADMIN)),
    /**
     * Create a new user
     */
    CREATE_USER(new CreateUserCommand(PathPage.PATH_ADMIN)),
    /**
     * Update user
     */
    UPDATE_USER(new UpdateUserCommand(PathPage.PATH_ADMIN));

    /**
     * Command
     */
    private Command command;

    /**
     * Constructor
     *
     * @param command - command
     */
    ActionType(Command command) {
        this.command = command;
    }

    /**
     * Get field value command
     *
     * @return command
     */
    public Command getCommand() {
        return command;
    }

    /**
     * Get path to page
     *
     * @return path
     */
    public String getJsp() {
        return command.getDirectoryPath() + name().toLowerCase() + ".jsp";
    }

    /**
     * Defining an action
     *
     * @param request - request
     * @return action
     */
    public static ActionType define(HttpServletRequest request) {
        try {
            String command = request.getParameter("command").toUpperCase();
            return ActionType.valueOf(command);
        } catch (Exception e) {
            Logger logger = LogManager.getLogger(EditUsersCommand.class);
            final HttpSession session = request.getSession(false);
            logger.log(Level.ERROR, e);
            session.setAttribute(AttributeName.ERROR, "108 ");
            return ActionType.INDEX;
        }
    }
}
