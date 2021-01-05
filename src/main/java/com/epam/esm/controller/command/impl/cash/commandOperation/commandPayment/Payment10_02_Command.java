package com.epam.esm.controller.command.impl.cash.commandOperation.commandPayment;

import com.epam.esm.controller.command.AttributeName;
import com.epam.esm.controller.command.Command;
import com.epam.esm.controller.command.ActionType;
import com.epam.esm.controller.check.PDFDocument;
import com.epam.esm.controller.check.CheckOperation10;
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
 * The action is "Payment 1002".
 */
public class Payment10_02_Command implements Command {
    private String directoryPath;
    private static final Logger logger = LogManager.getLogger(Payment10_02_Command.class);
    private final DutiesService dutiesService = new DutiesServiceImpl();
    private final CurrencyService currencyService = new CurrencyServiceImpl();
    private final PaymentService paymentService = new PaymentServiceImpl();
    private static final int NUMBER_OPERATION = 10;

    /**
     * Constructor
     *
     * @param directoryPath - path
     */
    public Payment10_02_Command(String directoryPath) {
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

        String today = DataUtil.getFormattedLocalDateStartDateTime(LocalDate.now());

        try {
            HttpSession session = request.getSession(false);
            List<Currency> currencies = currencyService.findAll();
            Long currencyIdSession = (Long) session.getAttribute(AttributeName.CURRENCY_ID);
            Long currencySumSession = (Long) session.getAttribute(AttributeName.CURRENCY_SUM);
            BigDecimal rateCBPaymentSession = (BigDecimal) session.getAttribute(AttributeName.RATE_CB_PAYMENT);
            BigDecimal sumRateCurrencyIdSession = (BigDecimal) session.getAttribute(AttributeName.SUM_RATE_CURRENCY_ID);
            String specificationSession = (String) session.getAttribute(AttributeName.SPECIFICATION);

            request.setAttribute(AttributeName.CURRENCY_ID, currencyIdSession);
            request.setAttribute(AttributeName.CURRENCY_SUM, currencySumSession);
            request.setAttribute(AttributeName.RATE_CB_PAYMENT, rateCBPaymentSession);
            request.setAttribute(AttributeName.SUM_RATE_CURRENCY_ID, DataUtil.round(sumRateCurrencyIdSession));
            request.setAttribute(AttributeName.SPECIFICATION, specificationSession);
            request.setAttribute(AttributeName.CURRENCIES, currencies);

            User user = ControllerDataUtil.findUser(request);
            if (dutiesService.openDutiesUserToday(user, today) == null) {
                return ActionType.DUTIES;
            }
            if (DataValidation.isCreateUpdateDeleteOperation(request)) {
                Map<Long, BigDecimal> values = ControllerDataUtil.getMapLongBigDecimal(request, AttributeName.ID, AttributeName.SUM);
                String specification = ControllerDataUtil.getString(request, AttributeName.SPECIFICATION);

                if (values.isEmpty()) {
                    return ActionType.PAYMENT;
                }

                Long operationId = paymentService.implementPayment10(values, rateCBPaymentSession, specification, user);
                Cookie localeCookie = ControllerDataUtil.getCookie(request, AttributeName.LOCAL);
                String locale = localeCookie != null && (!localeCookie.getValue().equals(AttributeName.LOCALE_RU)) ? AttributeName.LOCALE_EN : AttributeName.LOCALE_RU;
                PDFDocument document = new CheckOperation10();
                document.createCheck(operationId, NUMBER_OPERATION, locale);
                session.setAttribute(AttributeName.MESSAGE, "109 ");

                return ActionType.PAYMENT;
            }
            return ActionType.PAYMENT10_02;
        } catch (NegativeBalanceException e) {
            request.getSession(false).setAttribute(AttributeName.MESSAGE, "110 ");
            logger.log(Level.INFO, e);
            return ActionType.PAYMENT10_02;
        } catch (ServiceException | NumberFormatException e) {
            request.getSession(false).setAttribute(AttributeName.ERROR, "100 " + e);
            logger.log(Level.ERROR, e);
            return ActionType.ERROR;
        }
    }
}
