package com.epam.esm.controller.command.impl.cash.commandOperation.commandPayment;

import com.epam.esm.controller.command.AttributeName;
import com.epam.esm.controller.command.Command;
import com.epam.esm.controller.command.ActionType;
import com.epam.esm.util.ControllerDataUtil;
import com.epam.esm.util.DataValidation;
import com.epam.esm.entity.Currency;
import com.epam.esm.entity.Duties;
import com.epam.esm.entity.Kassa;
import com.epam.esm.entity.User;
import com.epam.esm.exception.CommandException;
import com.epam.esm.exception.ServiceException;
import com.epam.esm.model.service.CurrencyService;
import com.epam.esm.model.service.DutiesService;
import com.epam.esm.model.service.KassaService;
import com.epam.esm.model.service.PaymentService;
import com.epam.esm.model.service.impl.CurrencyServiceImpl;
import com.epam.esm.model.service.impl.DutiesServiceImpl;
import com.epam.esm.model.service.impl.KassaServiceImpl;
import com.epam.esm.model.service.impl.PaymentServiceImpl;
import com.epam.esm.util.DataUtil;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * The action is "Payment 1100 Balance".
 */
public class Payment1100BalanceCommand implements Command {
    private String directoryPath;
    private static final Logger logger = LogManager.getLogger(Payment1100BalanceCommand.class);
    private final DutiesService dutiesService = new DutiesServiceImpl();
    private final CurrencyService currencyService = new CurrencyServiceImpl();
    private final PaymentService paymentService = new PaymentServiceImpl();
    private final KassaService kassaService = new KassaServiceImpl();

    /**
     * Constructor
     *
     * @param directoryPath - path
     */
    public Payment1100BalanceCommand(String directoryPath) {
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
        final HttpSession session = request.getSession(false);

        LocalDate date = LocalDate.now();
        String today = DataUtil.getFormattedLocalDateStartDateTime(date);
        try {

            User user = ControllerDataUtil.findUser(request);
            Duties duties = dutiesService.openDutiesUserToday(user, today);
            if (duties == null) {
                return ActionType.DUTIES;
            }
            if (DataValidation.isCreateUpdateDeleteOperation(request)) {
                Map<Long, BigDecimal> values = ControllerDataUtil.getMapLongBigDecimal(request, AttributeName.ID, AttributeName.SUM);
                String specification = ControllerDataUtil.getString(request, AttributeName.SPECIFICATION);

                if (values.isEmpty()) {
                    return ActionType.PAYMENT;
                }
                paymentService.implementPayment1100(values, specification, user);
                session.setAttribute(AttributeName.MESSAGE, "109 ");
                return ActionType.PAYMENT;
            }

            List<Currency> currencies = currencyService.findAll();
            request.setAttribute(AttributeName.CURRENCIES, currencies);

            List<Kassa> balanceList = kassaService.getBalance(user, duties);
            request.setAttribute(AttributeName.BALANCE, balanceList);
            return ActionType.PAYMENT1100BALANCE;

        } catch (ServiceException | NumberFormatException e) {
            session.setAttribute(AttributeName.ERROR, "100 " + e);
            logger.log(Level.ERROR, e);
            return ActionType.ERROR;
        }
    }
}
