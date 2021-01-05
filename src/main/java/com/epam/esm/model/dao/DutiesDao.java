package com.epam.esm.model.dao;

import com.epam.esm.entity.Duties;
import com.epam.esm.entity.User;
import com.epam.esm.exception.DaoException;

import java.util.List;

public interface DutiesDao extends Dao<Duties> {
    /**
     * Get a list of open user duties
     *
     * @return list of user duties
     * @throws DaoException database access error or other errors
     */
    List<Duties> openDutiesUserToday(Long userId, String today) throws DaoException;

    /**
     * Get number of user duties
     *
     * @return duties number
     * @throws DaoException database access error or other errors
     */
    Integer numberDutiesToday(User user, String today) throws DaoException;

}
