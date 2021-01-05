package com.epam.esm.controller.command.impl;

import com.epam.esm.controller.command.ActionType;
import com.epam.esm.controller.command.Command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * The action is "Start page".
 */
public class IndexCommand implements Command {
    private String directoryPath;

    /**
     * Constructor
     *
     * @param directoryPath - path
     */
    public IndexCommand(String directoryPath) {
        this.directoryPath = directoryPath;
    }

    @Override
    public String getDirectoryPath() {
        return directoryPath;
    }

    @Override
    public ActionType execute(HttpServletRequest request, HttpServletResponse response) {
        return ActionType.INDEX;
    }
}
