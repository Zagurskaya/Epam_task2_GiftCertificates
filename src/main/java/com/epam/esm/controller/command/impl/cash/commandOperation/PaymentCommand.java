package com.epam.esm.controller.command.impl.cash.commandOperation;

import com.epam.esm.controller.command.ActionType;
import com.epam.esm.controller.command.AttributeName;
import com.epam.esm.controller.command.Command;
import com.epam.esm.util.ControllerDataUtil;
import com.epam.esm.entity.SprOperation;
import com.epam.esm.entity.User;
import com.epam.esm.exception.ServiceException;
import com.epam.esm.model.service.DutiesService;
import com.epam.esm.model.service.PaymentService;
import com.epam.esm.model.service.impl.DutiesServiceImpl;
import com.epam.esm.model.service.impl.PaymentServiceImpl;
import com.epam.esm.util.DataUtil;
import com.epam.esm.util.DataValidation;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.util.List;

/**
 * The action is "Payment".
 */
public class PaymentCommand implements Command {
    private String directoryPath;
    private static final Logger logger = LogManager.getLogger(PaymentCommand.class);
    private final DutiesService dutiesService = new DutiesServiceImpl();
    private final PaymentService paymentService = new PaymentServiceImpl();
    private static final String SPR_OPERATION_ID = "SprOperationsId";

    /**
     * Constructor
     *
     * @param directoryPath - path
     */
    public PaymentCommand(String directoryPath) {
        this.directoryPath = directoryPath;
    }

    @Override
    public String getDirectoryPath() {
        return directoryPath;
    }

    @Override
    public ActionType execute(HttpServletRequest request, HttpServletResponse response) {
        ControllerDataUtil.removeAttributeError(request);

        LocalDate date = LocalDate.now();
        String today = DataUtil.getFormattedLocalDateStartDateTime(date);
        try {
            User user = ControllerDataUtil.findUser(request);
            if (dutiesService.openDutiesUserToday(user, today) == null) {
                return ActionType.DUTIES;
            }
            if (DataValidation.isCreateUpdateDeleteOperation(request)) {
                Long sprOperationsId = DataUtil.getLong(request, SPR_OPERATION_ID);
                SprOperation sprOperation = paymentService.findSprOperationById(sprOperationsId);

                if (sprOperation != null) {
                    request.setAttribute(AttributeName.SPR_OPERATION, sprOperation);
                }
                switch (sprOperationsId.toString()) {
                    case "10":
                        return ActionType.PAYMENT10_01;
                    case "20":
                        return ActionType.PAYMENT20_01;
                    case "998":
                        return ActionType.PAYMENT998;
                    case "1000":
                        return ActionType.PAYMENT1000;
                    case "1100":
                        return ActionType.PAYMENT1100;
                    default:
                        return ActionType.PAYMENT;
                }
            }
            List<SprOperation> sprOperations = paymentService.findAllSprOperation();
            request.setAttribute(AttributeName.SPR_OPERATIONS, sprOperations);
            return ActionType.PAYMENT;

        } catch (ServiceException | NumberFormatException e) {
            request.getSession(false).setAttribute(AttributeName.ERROR, "100 " + e);
            logger.log(Level.ERROR, e);
            return ActionType.ERROR;
        }
    }

}
