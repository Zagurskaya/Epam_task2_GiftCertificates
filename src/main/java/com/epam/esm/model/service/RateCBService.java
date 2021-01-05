package com.epam.esm.model.service;

import com.epam.esm.entity.RateCB;
import com.epam.esm.exception.CommandException;
import com.epam.esm.exception.ServiceException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface RateCBService extends Service<RateCB> {

    /**
     * Get the rate CB by values coming/spending
     *
     * @param now      - date and time of the rate
     * @param coming   - code currency by the coming
     * @param spending - code currency by the spending
     * @return value rate CB
     * @throws ServiceException error during execution of logical blocks and actions
     */
    BigDecimal rateCBToday(LocalDateTime now, Long coming, Long spending) throws ServiceException;

    /**
     * Create List of rateCB
     *
     * @param rateCBList - List of rateCB
     * @throws ServiceException error during execution of logical blocks and actions
     * @throws CommandException volition error
     */
    void create(List<RateCB> rateCBList) throws ServiceException, CommandException;

    /**
     * Get map with last rateCB today
     *
     * @return Map with last rates CB
     * @throws ServiceException error during execution of logical blocks and actions
     */
    List<RateCB> allLastRateCB() throws ServiceException;
}
