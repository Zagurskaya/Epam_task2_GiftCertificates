package com.epam.esm.model.dao;

import com.epam.esm.entity.RateNB;
import com.epam.esm.exception.DaoException;

import java.time.LocalDate;

public interface RateNBDao extends Dao<RateNB> {
    /**
     * Find the latest rate NB by currency
     *
     * @return rate NB
     * @throws DaoException database access error or other errors
     */
    RateNB rateNBToday(LocalDate date, Long currencyId) throws DaoException;
}
