package com.epam.esm.controller.command.impl.cash.commandOperation;

import com.epam.esm.controller.command.AttributeName;
import com.epam.esm.controller.command.Command;
import com.epam.esm.controller.command.ActionType;
import com.epam.esm.util.ControllerDataUtil;
import com.epam.esm.entity.Currency;
import com.epam.esm.entity.Duties;
import com.epam.esm.entity.Kassa;
import com.epam.esm.entity.User;
import com.epam.esm.exception.ServiceException;
import com.epam.esm.model.service.CurrencyService;
import com.epam.esm.model.service.DutiesService;
import com.epam.esm.model.service.KassaService;
import com.epam.esm.model.service.impl.CurrencyServiceImpl;
import com.epam.esm.model.service.impl.DutiesServiceImpl;
import com.epam.esm.model.service.impl.KassaServiceImpl;
import com.epam.esm.util.DataUtil;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.util.List;

/**
 * The action is "Balance".
 */
public class BalanceCommand implements Command {
    private String directoryPath;
    private static final Logger logger = LogManager.getLogger(BalanceCommand.class);
    private final DutiesService dutiesService = new DutiesServiceImpl();
    private final CurrencyService currencyService = new CurrencyServiceImpl();
    private final KassaService kassaService = new KassaServiceImpl();

    /**
     * Constructor
     *
     * @param directoryPath - path
     */
    public BalanceCommand(String directoryPath) {
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
            List<Kassa> balanceList = kassaService.getBalance(user, duties);
            request.setAttribute(AttributeName.BALANCE, balanceList);
            List<Currency> currencies = currencyService.findAll();
            request.setAttribute(AttributeName.CURRENCIES, currencies);
            return ActionType.BALANCE;

        } catch (ServiceException | NumberFormatException e) {
            request.getSession(false).setAttribute(AttributeName.ERROR, "100 " + e);
            logger.log(Level.ERROR, e);
            return ActionType.ERROR;
        }
    }
}
