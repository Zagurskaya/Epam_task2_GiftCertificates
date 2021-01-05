package com.epam.esm.controller.command.impl.cash.commandCurrency;

import com.epam.esm.controller.command.ActionType;
import com.epam.esm.controller.command.AttributeName;
import com.epam.esm.controller.command.Command;
import com.epam.esm.util.ControllerDataUtil;
import com.epam.esm.entity.Currency;
import com.epam.esm.exception.ServiceException;
import com.epam.esm.model.service.CurrencyService;
import com.epam.esm.model.service.impl.CurrencyServiceImpl;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * The action is "Currency".
 */
public class AllCurrencyCommand implements Command {
    private String directoryPath;
    private static final Logger logger = LogManager.getLogger(AllCurrencyCommand.class);
    private final CurrencyService currencyService = new CurrencyServiceImpl();

    /**
     * Constructor
     *
     * @param directoryPath - path
     */
    public AllCurrencyCommand(String directoryPath) {
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

        try {
            int page = 1;
            if (request.getParameter(AttributeName.PAGE) != null)
                page = Integer.parseInt(request.getParameter(AttributeName.PAGE));

            int numberOfPages = (int) Math.ceil(currencyService.countRows() * 1.0 / AttributeName.RECORDS_PER_PAGE);
            List<Currency> currencies = currencyService.onePartOfListOnPage(page);

            request.setAttribute(AttributeName.NUMBER_OF_PAGE, numberOfPages);
            request.setAttribute(AttributeName.CURRENT_PAGE, page);
            request.setAttribute(AttributeName.CURRENCIES, currencies);
            return ActionType.ALL_CURRENCY;

        } catch (ServiceException e) {
            request.getSession(false).setAttribute(AttributeName.ERROR, e);
            logger.log(Level.ERROR, e);
            return ActionType.ERROR;
        }
    }
}
