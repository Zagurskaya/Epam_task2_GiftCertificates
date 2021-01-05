package com.epam.esm.model.service;

import com.epam.esm.entity.Currency;
import com.epam.esm.exception.ServiceException;

import java.util.List;
import java.util.Map;

public interface CurrencyService extends Service<Currency> {
    /**
     * Get a list of currencies
     *
     * @return currency list
     * @throws ServiceException error during execution of logical blocks and actions
     */
    List findAll() throws ServiceException;

    /**
     * Get a map with currency's id and currency's ISO
     *
     * @return currency map
     * @throws ServiceException error during execution of logical blocks and actions
     */
    Map<String, String> findAllToMap() throws ServiceException;

    /**
     * Get a currency's list for sale/buy operation
     *
     * @return currency list
     * @throws ServiceException error during execution of logical blocks and actions
     */
    List findAllCurrenciesForSaleAndBuy() throws ServiceException;

}
