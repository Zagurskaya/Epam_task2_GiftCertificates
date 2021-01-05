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

    /**
     * Table 'currency'
     */
    public static final String CURRENCY_ID = "id";
    public static final String CURRENCY_ISO = "iso";
    public static final String CURRENCY_NAME_RU = "nameRU";
    public static final String CURRENCY_NAME_EN = "nameEN";

    /**
     * Table 'rateNB'
     */
    public static final String RATENB_ID = "id";
    public static final String RATENB_CURRENCY_ID = "currencyId";
    public static final String RATENB_DATE = "date";
    public static final String RATENB_SUM = "sum";

    /**
     * Table 'rateCB'
     */
    public static final String RATECB_ID = "id";
    public static final String RATECB_COMING = "coming";
    public static final String RATECB_SPENDING = "spending";
    public static final String RATECB_TIMESTAMP = "timestamp";
    public static final String RATECB_SUM = "sum";
    public static final String RATECB_IS_BACK = "isBack";

    /**
     * Table 'duties'
     */
    public static final String DUTIES_ID = "id";
    public static final String DUTIES_USER_ID = "userId";
    public static final String DUTIES_TIMESTAMP = "timestamp";
    public static final String DUTIES_NUMBER = "number";
    public static final String DUTIES_IS_CLOSE = "isClose";

    /**
     * Table 'kassa'
     */
    public static final String KASSA_ID = "id";
    public static final String KASSA_CURRENCY_ID = "currencyId";
    public static final String KASSA_RESEIVED = "received";
    public static final String KASSA_COMING = "coming";
    public static final String KASSA_SPENDING = "spending";
    public static final String KASSA_TRANSMITTED = "transmitted";
    public static final String KASSA_BALANCE = "balance";
    public static final String KASSA_USER_ID = "userId";
    public static final String KASSA_DATE = "date";
    public static final String KASSA_DUTIES_ID = "dutiesId";

    /**
     * Table 'sproperation'
     */
    public static final String SPR_OPERATION_ID = "id";
    public static final String SPR_OPERATION_NAME_RU = "nameRU";
    public static final String SPR_OPERATION_NAME_EN = "nameEN";
    public static final String SPR_OPERATION_SPECIFICATION = "specification";

    /**
     * Table 'sprentry'
     */
    public static final String SPR_ENTRY_ID = "id";
    public static final String SPR_ENTRY_NAME = "name";
    public static final String SPR_ENTRY_CURRENCY_ID = "currencyId";
    public static final String SPR_ENTRY_SPR_OPERATION_ID = "sprOperationId";
    public static final String SPR_ENTRY_ACCOUNT_DEBIT = "accountDebit";
    public static final String SPR_ENTRY_ACCOUNT_CREDIT = "accountCredit";
    public static final String SPR_ENTRY_IS_SPENDING = "isSpending";
    public static final String SPR_ENTRY_RATE = "rate";

    /**
     * Table 'useroperation'
     */
    public static final String USER_OPERATION_ID = "id";
    public static final String USER_OPERATION_TIMESTAMP = "timestamp";
    public static final String USER_OPERATION_RATE = "rate";
    public static final String USER_OPERATION_SUM = "sum";
    public static final String USER_OPERATION_CURRENCY_ID = "currencyId";
    public static final String USER_OPERATION_USER_ID = "userId";
    public static final String USER_OPERATION_DUTIES_ID = "dutiesId";
    public static final String USER_OPERATION_OPERATION_ID = "operationId";
    public static final String USER_OPERATION_SPECIFICATION = "specification";
    public static final String USER_OPERATION_CHECKING_ACCOUNT = "checkingAccount";
    public static final String USER_OPERATION_FULL_NAME = "fullName";

    /**
     * Table 'userentry'
     */
    public static final String USER_ENTRY_ID = "id";
    public static final String USER_ENTRY_USER_OPERATION_ID = "userOperationId";
    public static final String USER_ENTRY_SPR_ENTRY_ID = "sprEntryId";
    public static final String USER_ENTRY_CURRENCY_ID = "currencyId";
    public static final String USER_ENTRY_ACCOUNT_DEBIT = "accountDebit";
    public static final String USER_ENTRY_ACCOUNT_CREDIT = "accountCredit";
    public static final String USER_ENTRY_SUM = "sum";
    public static final String USER_ENTRY_IS_SPENDING = "isSpending";
    public static final String USER_ENTRY_RATE = "rate";

    private ColumnName() {
    }
}
