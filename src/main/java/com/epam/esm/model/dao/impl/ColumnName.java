package com.epam.esm.model.dao.impl;

/**
 * Name of the table columns in the database
 */
public class ColumnName {
    // format -> TABLE_NAME

    /**
     * Table 'user'
     */
    public static final String USER_ID = "id";
    public static final String USER_LOGIN = "login";
    public static final String USER_PASSWORD = "password";
    public static final String USER_FULL_NAME = "fullname";
    public static final String USER_ROLE = "role";

    public static final String TAG_ID = "id";
    public static final String TAG_NAME = "name";

    private ColumnName() {
    }
}
