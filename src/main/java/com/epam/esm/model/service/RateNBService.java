package com.epam.esm.model.service;

import com.epam.esm.entity.RateNB;
import com.epam.esm.exception.CommandException;
import com.epam.esm.exception.ServiceException;

import java.util.List;

public interface RateNBService extends Service<RateNB> {
    /**
     * Create List of rateNB
     *
     * @param rateNBList - List of rateNB
     * @throws ServiceException error during execution of logical blocks and actions
     * @throws CommandException volition error
     */
    void create(List<RateNB> rateNBList) throws ServiceException, CommandException;
}
