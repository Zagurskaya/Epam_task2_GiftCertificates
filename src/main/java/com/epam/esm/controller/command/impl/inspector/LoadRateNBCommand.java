package com.epam.esm.controller.command.impl.inspector;

import com.epam.esm.controller.command.ActionType;
import com.epam.esm.controller.command.Command;
import com.epam.esm.util.ControllerDataUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * The action is "Loading rate CB".
 */
public class LoadRateNBCommand implements Command {
    private String directoryPath;

    /**
     * Constructor
     *
     * @param directoryPath - path
     */
    public LoadRateNBCommand(String directoryPath) {
        this.directoryPath = directoryPath;
    }

    @Override
    public String getDirectoryPath() {
        return directoryPath;
    }

    @Override
    public ActionType execute(HttpServletRequest request, HttpServletResponse response) {
        ControllerDataUtil.removeAttributeMessage(request);
        ControllerDataUtil.removeAttributeError(request);
        return ActionType.LOAD_RATE_NB;
    }
}
