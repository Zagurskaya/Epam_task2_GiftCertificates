package com.epam.esm.controller.command.impl.cash.commandOperation.commandPayment;

import com.epam.esm.controller.command.AttributeName;
import com.epam.esm.controller.command.Command;
import com.epam.esm.controller.command.ActionType;
import com.epam.esm.controller.check.PDFDocument;
import com.epam.esm.controller.check.CheckOperation998;
import com.epam.esm.exception.NegativeBalanceException;
import com.epam.esm.util.ControllerDataUtil;
import com.epam.esm.util.DataValidation;
import com.epam.esm.entity.Currency;
import com.epam.esm.entity.User;
import com.epam.esm.exception.CommandException;
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

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * The action is "Payment 998".
 */
public class Payment998_Command implements Command {
    private String directoryPath;
    private static final Logger logger = LogManager.getLogger(Payment1000_Command.class);
    private final DutiesService dutiesService = new DutiesServiceImpl();
    private final CurrencyService currencyService = new CurrencyServiceImpl();
    private final PaymentService paymentService = new PaymentServiceImpl();
    private static final int NUMBER_OPERATION = 998;


    /**
     * Constructor
     *
     * @param directoryPath - path
     */
    public Payment998_Command(String directoryPath) {
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
            if (dutiesService.openDutiesUserToday(user, today) == null) {
                return ActionType.DUTIES;
            }
            if (DataValidation.isCreateUpdateDeleteOperation(request)) {
                Map<Long, BigDecimal> values = ControllerDataUtil.getMapLongBigDecimal(request, AttributeName.ID, AttributeName.SUM);
                String specification = ControllerDataUtil.getString(request, AttributeName.SPECIFICATION);
                String checkingAccount = ControllerDataUtil.getString(request, AttributeName.CHECKING_ACCOUNT);
                String fullName = ControllerDataUtil.getString(request, AttributeName.FULL_MANE);

                if (values.isEmpty()) {
                    return ActionType.PAYMENT;
                }
                Long operationId = paymentService.implementPayment998(values, specification, checkingAccount, fullName, user);
                Cookie localeCookie = ControllerDataUtil.getCookie(request, AttributeName.LOCAL);
                String locale = localeCookie != null && (!localeCookie.getValue().equals(AttributeName.LOCALE_RU)) ? AttributeName.LOCALE_EN : AttributeName.LOCALE_RU;
                PDFDocument document = new CheckOperation998();
                document.createCheck(operationId, NUMBER_OPERATION, locale);
                session.setAttribute(AttributeName.MESSAGE, "109 ");
                return ActionType.PAYMENT;
            }

            List<Currency> currencies = currencyService.findAll();
            request.getSession(false).setAttribute(AttributeName.CURRENCIES, currencies);
            return ActionType.PAYMENT998;
        } catch (NegativeBalanceException e) {
            request.getSession(false).setAttribute(AttributeName.MESSAGE, "110 ");
            logger.log(Level.INFO, e);
            return ActionType.PAYMENT998;
        } catch (ServiceException | NumberFormatException e) {
            request.getSession(false).setAttribute(AttributeName.ERROR, "100 " + e);
            logger.log(Level.ERROR, e);
            return ActionType.ERROR;
        }
    }
}