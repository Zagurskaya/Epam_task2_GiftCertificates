package com.epam.esm.controller.command.impl.cash.commandOperation.commandPayment;

import com.epam.esm.controller.command.AttributeName;
import com.epam.esm.controller.command.Command;
import com.epam.esm.controller.command.ActionType;
import com.epam.esm.util.ControllerDataUtil;
import com.epam.esm.util.DataValidation;
import com.epam.esm.entity.Currency;
import com.epam.esm.entity.User;
import com.epam.esm.exception.CommandException;
import com.epam.esm.exception.ServiceException;
import com.epam.esm.model.service.CurrencyService;
import com.epam.esm.model.service.DutiesService;
import com.epam.esm.model.service.RateCBService;
import com.epam.esm.model.service.impl.CurrencyServiceImpl;
import com.epam.esm.model.service.impl.DutiesServiceImpl;
import com.epam.esm.model.service.impl.RateCBServiceImpl;
import com.epam.esm.util.DataUtil;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * The action is "Payment 1001".
 */
public class Payment10_01_Command implements Command {
    private String directoryPath;
    private static final Logger logger = LogManager.getLogger(Payment10_01_Command.class);
    private final DutiesService dutiesService = new DutiesServiceImpl();
    private final CurrencyService currencyService = new CurrencyServiceImpl();
    private final RateCBService rateCBService = new RateCBServiceImpl();


    /**
     * Constructor
     *
     * @param directoryPath - path
     */
    public Payment10_01_Command(String directoryPath) {
        this.directoryPath = directoryPath;
    }

    @Override
    public String getDirectoryPath() {
        return directoryPath;
    }

    @Override
    public ActionType execute(HttpServletRequest request, HttpServletResponse response) throws CommandException {
        ControllerDataUtil.removeAttributeError(request);
        ControllerDataUtil.removeAttributeMessage(request);

        LocalDate date = LocalDate.now();
        LocalDateTime now = LocalDateTime.now();
        String today = DataUtil.getFormattedLocalDateStartDateTime(date);
        try {
            User user = ControllerDataUtil.findUser(request);
            if (dutiesService.openDutiesUserToday(user, today) == null) {
                return ActionType.DUTIES;
            }
            if (DataValidation.isCreateUpdateDeleteOperation(request)) {
                Long currencyId = ControllerDataUtil.getLong(request, AttributeName.ID);
                Long currencySum = ControllerDataUtil.getLong(request, AttributeName.SUM);
                String specification = ControllerDataUtil.getString(request, AttributeName.SPECIFICATION);
                if (currencyId == null || currencySum == null) {
                    return ActionType.PAYMENT10_01;
                }
                BigDecimal rateCBPayment = rateCBService.rateCBToday(now, currencyId, AttributeName.NС);
                BigDecimal sumRateCurrencyId = rateCBPayment.multiply(BigDecimal.valueOf(currencySum));

                HttpSession session = request.getSession(false);
                session.setAttribute("currencyId", currencyId);
                session.setAttribute("currencySum", currencySum);
                session.setAttribute("rateCBPayment", rateCBPayment);
                session.setAttribute("sumRateCurrencyId", sumRateCurrencyId);
                session.setAttribute("specification", specification);

                return ActionType.PAYMENT10_02;
            }

            List<Currency> currencies = currencyService.findAllCurrenciesForSaleAndBuy();
            request.getSession(false).setAttribute(AttributeName.CURRENCIES, currencies);
            return ActionType.PAYMENT10_01;

        } catch (ServiceException | NumberFormatException e) {
            request.getSession(false).setAttribute(AttributeName.ERROR, "100 " + e);
            logger.log(Level.ERROR, e);
            return ActionType.ERROR;
        }
    }
}
