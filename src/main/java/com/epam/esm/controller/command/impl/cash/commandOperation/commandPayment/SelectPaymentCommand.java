package com.epam.esm.controller.command.impl.cash.commandOperation.commandPayment;

import com.epam.esm.controller.command.Command;
import com.epam.esm.controller.command.ActionType;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * The action is "Select Payment".
 */
public class SelectPaymentCommand implements Command {
    private String directoryPath;

    /**
     * Constructor
     *
     * @param directoryPath - path
     */
    public SelectPaymentCommand(String directoryPath) {
        this.directoryPath = directoryPath;
    }

    @Override
    public String getDirectoryPath() {
        return directoryPath;
    }

    @Override
    public ActionType execute(HttpServletRequest request, HttpServletResponse response) {
        return ActionType.SELECT_PAYMENT;
    }
}
