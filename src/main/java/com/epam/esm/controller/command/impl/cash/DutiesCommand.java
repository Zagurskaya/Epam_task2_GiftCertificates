package com.epam.esm.controller.command.impl.cash;

import com.epam.esm.controller.command.ActionType;
import com.epam.esm.controller.command.AttributeName;
import com.epam.esm.controller.command.Command;
import com.epam.esm.model.service.KassaService;
import com.epam.esm.model.service.impl.KassaServiceImpl;
import com.epam.esm.util.ControllerDataUtil;
import com.epam.esm.entity.Duties;
import com.epam.esm.entity.User;
import com.epam.esm.exception.ServiceException;
import com.epam.esm.model.service.DutiesService;
import com.epam.esm.model.service.UserService;
import com.epam.esm.model.service.impl.DutiesServiceImpl;
import com.epam.esm.model.service.impl.UserServiceImpl;
import com.epam.esm.util.DataUtil;
import com.epam.esm.util.DataValidation;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.time.LocalDate;
import java.util.List;

public class DutiesCommand implements Command {
    private String directoryPath;
    private static final Logger logger = LogManager.getLogger(DutiesCommand.class);
    private final DutiesService dutiesService = new DutiesServiceImpl();
    private final UserService userService = new UserServiceImpl();
    private final KassaService kassaService = new KassaServiceImpl();

    /**
     * Constructor
     *
     * @param directoryPath - path
     */
    public DutiesCommand(String directoryPath) {
        this.directoryPath = directoryPath;
    }

    @Override
    public String getDirectoryPath() {
        return directoryPath;
    }

    @Override
    public ActionType execute(HttpServletRequest request, HttpServletResponse response) {
        ControllerDataUtil.removeAttributeError(request);
        ControllerDataUtil.removeAttributeMessage(request);
        HttpSession session = request.getSession(false);
        LocalDate date = LocalDate.now();
        String today = DataUtil.getFormattedLocalDateStartDateTime(date);
        try {
            User user = ControllerDataUtil.findUser(request);

            if (DataValidation.isCreateUpdateDeleteOperation(request)) {
                if (DataValidation.isOpenOperation(request)) {
                    if (dutiesService.openDutiesUserToday(user, today) == null) {
                        dutiesService.openNewDuties(user);
                    }
                    setAttributeToRequest(request, user);
                    return ActionType.DUTIES;
                } else if (DataValidation.isCloseOperation(request)) {
                    Duties duties = dutiesService.openDutiesUserToday(user, today);
                    if (duties == null) {
                        request.setAttribute(AttributeName.DUTIES_MESSAGE, "202 " + user.getFullName());
                        return ActionType.DUTIES;
                    }
                    if (!kassaService.isBalanceValid(user, duties)) {
                        setAttributeToRequest(request, user);
                        request.setAttribute(AttributeName.DUTIES_MESSAGE, "204 ");
                        return ActionType.DUTIES;
                    }
                    dutiesService.closeOpenDutiesUserToday(user);
                    setAttributeToRequest(request, user);
                    return ActionType.DUTIES;
                }
            }
            setAttributeToRequest(request, user);
            return ActionType.DUTIES;
        } catch (ServiceException e) {
            session.setAttribute(AttributeName.ERROR, "100 " + e);
            logger.log(Level.ERROR, e);
            return ActionType.ERROR;
        }
    }

    private void setAttributeToRequest(HttpServletRequest request, User user) throws ServiceException {
        LocalDate date = LocalDate.now();
        String today = DataUtil.getFormattedLocalDateStartDateTime(date);

        List<User> users = userService.findAll();
        Duties duties = dutiesService.openDutiesUserToday(user, today);
        String messageDuties = (duties != null) ? "201 " + user.getFullName() : "202 " + user.getFullName();


        request.setAttribute(AttributeName.USERS, users);
        request.setAttribute(AttributeName.DUTIES, duties);
        request.setAttribute(AttributeName.DUTIES_MESSAGE, messageDuties);
    }
}
