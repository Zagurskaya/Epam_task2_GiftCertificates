package com.epam.esm.controller.command.impl.inspector;

import com.epam.esm.controller.command.ActionType;
import com.epam.esm.controller.command.AttributeName;
import com.epam.esm.controller.command.Command;
import com.epam.esm.entity.UserEntry;
import com.epam.esm.exception.ServiceException;
import com.epam.esm.model.service.UserEntryService;
import com.epam.esm.model.service.impl.UserEntryServiceImpl;
import com.epam.esm.util.ControllerDataUtil;
import com.epam.esm.util.DataProperty;
import com.epam.esm.util.DataUtil;
import com.epam.esm.util.DataValidation;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.List;


/**
 * The action is "Unloading entries".
 */
public class UnloadEntriesCommand implements Command {
    private String directoryPath;
    private static final Logger logger = LogManager.getLogger(UnloadEntriesCommand.class);
    private final UserEntryService userEntryService = new UserEntryServiceImpl();
    String OUT_PATH = DataProperty.outPatch;

    /**
     * Constructor
     *
     * @param directoryPath - path
     */
    public UnloadEntriesCommand(String directoryPath) {
        this.directoryPath = directoryPath;
    }

    @Override
    public String getDirectoryPath() {
        return directoryPath;
    }

    @Override
    public ActionType execute(HttpServletRequest request, HttpServletResponse response) {
        ControllerDataUtil.removeAttributeMessage(request);
        ControllerDataUtil.removeAttributeError(request);
        LocalDate date = LocalDate.now();
        try {
            if (DataValidation.isCreateUpdateDeleteOperation(request)) {
                List<UserEntry> userEntryList = userEntryService.findAllToday();
                String fileName = OUT_PATH + "entries" + DataUtil.getFormattedLocalDateToString(date) + ".txt";
                FileWriter fileWriter = new FileWriter(fileName);
                PrintWriter printWriter = new PrintWriter(fileWriter);
                userEntryList.forEach(userEntry ->
                        printWriter.print(userEntry.getCurrencyId().toString() + ";" +
                                userEntry.getSum().toString() + ";" +
                                userEntry.getAccountCredit() + ";" +
                                userEntry.getAccountDebit() + "\n")
                );
                printWriter.close();
                request.setAttribute(AttributeName.MESSAGE, "File Unloaded Successfully");
                return ActionType.UNLOAD_ENTRIES;
            }
            request.setAttribute(AttributeName.MESSAGE, "Data = " + date);
            return ActionType.UNLOAD_ENTRIES;
        } catch (ServiceException | IOException e) {
            request.getSession(false).setAttribute(AttributeName.ERROR, "100 " + e);
            logger.log(Level.ERROR, e);
            return ActionType.ERROR;
        }
    }
}
