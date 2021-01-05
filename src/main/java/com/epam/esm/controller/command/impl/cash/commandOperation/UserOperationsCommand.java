package com.epam.esm.controller.command.impl.cash.commandOperation;

import com.epam.esm.controller.command.ActionType;
import com.epam.esm.controller.command.AttributeName;
import com.epam.esm.controller.command.Command;
import com.epam.esm.util.ControllerDataUtil;
import com.epam.esm.entity.Currency;
import com.epam.esm.entity.Duties;
import com.epam.esm.entity.SprOperation;
import com.epam.esm.entity.User;
import com.epam.esm.entity.UserOperation;
import com.epam.esm.exception.ServiceException;
import com.epam.esm.model.service.CurrencyService;
import com.epam.esm.model.service.DutiesService;
import com.epam.esm.model.service.PaymentService;
import com.epam.esm.model.service.impl.CurrencyServiceImpl;
import com.epam.esm.model.service.impl.DutiesServiceImpl;
import com.epam.esm.model.service.impl.PaymentServiceImpl;
import com.epam.esm.util.DataUtil;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.util.List;

/**
 * The action is "User operation list".
 */
public class UserOperationsCommand implements Command {
    private String directoryPath;
    private static final Logger logger = LogManager.getLogger(UserOperationsCommand.class);
    private final DutiesService dutiesService = new DutiesServiceImpl();
    private final CurrencyService currencyService = new CurrencyServiceImpl();
    private final PaymentService paymentService = new PaymentServiceImpl();

    /**
     * Constructor
     *
     * @param directoryPath - path
     */
    public UserOperationsCommand(String directoryPath) {
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

        LocalDate date = LocalDate.now();
        String today = DataUtil.getFormattedLocalDateStartDateTime(date);
        try {
            User user = ControllerDataUtil.findUser(request);
            Duties duties = dutiesService.openDutiesUserToday(user, today);
            if (duties == null) {
                return ActionType.DUTIES;
            }
            int page = 1;
            if (request.getParameter(AttributeName.PAGE) != null)
                page = Integer.parseInt(request.getParameter(AttributeName.PAGE));

            int numberOfPages = (int) Math.ceil(paymentService.countRowsUserOperations(user, duties) * 1.0 / AttributeName.RECORDS_PER_PAGE);
            List<UserOperation> userAllOperationList = paymentService.onePartOfListUserOperationsOnPage(user, duties, page);
            List<SprOperation> sprOperationsList = paymentService.findAllSprOperation();
            List<Currency> currencyList = currencyService.findAll();

            request.setAttribute(AttributeName.SPR_OPERATIONS, sprOperationsList);
            request.setAttribute(AttributeName.CURRENCIES, currencyList);
            request.setAttribute(AttributeName.USER_OPERATIONS, userAllOperationList);
            request.setAttribute(AttributeName.NUMBER_OF_PAGE, numberOfPages);
            request.setAttribute(AttributeName.CURRENT_PAGE, page);

            return ActionType.USER_OPERATIONS;
        } catch (ServiceException | NumberFormatException e) {
            request.getSession(false).setAttribute(AttributeName.ERROR, "100 " + e);
            logger.log(Level.ERROR, e);
            return ActionType.ERROR;
        }
    }
}
