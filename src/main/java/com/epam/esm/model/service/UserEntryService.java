package com.epam.esm.model.service;

import com.epam.esm.entity.UserEntry;
import com.epam.esm.exception.ServiceException;

import java.util.List;

public interface UserEntryService {

    /**
     * Get entry List by today
     *
     * @return user List
     */
    List<UserEntry> findAllToday() throws ServiceException;
}
