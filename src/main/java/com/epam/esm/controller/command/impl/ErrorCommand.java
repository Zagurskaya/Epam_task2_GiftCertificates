package com.epam.esm.controller.command.impl;

import com.epam.esm.controller.command.ActionType;
import com.epam.esm.controller.command.AttributeName;
import com.epam.esm.controller.command.Command;
import com.epam.esm.util.ControllerDataUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * The action is "Error page".
 */
public class ErrorCommand implements Command {
    private String directoryPath;

    /**
     * Constructor
     *
     * @param directoryPath - path
     */
    public ErrorCommand(String directoryPath) {
        this.directoryPath = directoryPath;
    }

    @Override
    public String getDirectoryPath() {
        return directoryPath;
    }

    @Override
    public ActionType execute(HttpServletRequest request, HttpServletResponse response) {
        final HttpSession session = request.getSession(false);
        request.setAttribute(AttributeName.ERROR, session.getAttribute(AttributeName.ERROR));
        ControllerDataUtil.removeAttributeError(request);
        return ActionType.ERROR;
    }
}
