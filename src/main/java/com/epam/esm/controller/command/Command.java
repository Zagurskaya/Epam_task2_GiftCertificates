package com.epam.esm.controller.command;

import com.epam.esm.exception.CommandException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Command
 */
public interface Command {
    /**
     * Path to jsp page
     *
     * @return путь
     */
    String getDirectoryPath();

    /**
     * Defining an action
     *
     * @param request - request
     * @return action
     */
    ActionType execute(HttpServletRequest request, HttpServletResponse response) throws CommandException;
}
