package com.epam.esm.util;

import com.epam.esm.exception.CommandException;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class DataUtil {
    private static final Logger logger = LogManager.getLogger(DataUtil.class);

    /**
     * Get a string matching the pattern
     *
     * @param value   - matching value
     * @param pattern - matching pattern
     * @return true on successful update
     * @throws CommandException validation error
     */
    public static String getString(String value, String pattern) throws CommandException {
        if (value.matches(pattern))
            return ControllerDataUtil.convertHtmlSpecialChars(value);
        else {
            logger.log(Level.ERROR, "Value  incorrect" + ControllerDataUtil.convertHtmlSpecialChars(value));
            throw new CommandException("102" + ControllerDataUtil.convertHtmlSpecialChars(value));
        }
    }

    /**
     * Get long type from request parameter
     *
     * @param req  - request
     * @param name - parameter name
     * @return long type value
     */
    public static Long getLong(HttpServletRequest req, String name) {
        String value = req.getParameter(name);
        return Long.parseLong(value);
    }

    /**
     * Format date : yyyy-MM-dd 00:00:00.000
     *
     * @param localDate - date
     * @return string type date
     */
    public static String getFormattedLocalDateStartDateTime(LocalDate localDate) {
        return localDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd 00:00:00.000"));
    }

    /**
     * Format date :  yyyyddMMHHmmss
     *
     * @param localDateTime - date and time
     * @return string type date
     */
    public static String getFormattedLocalDateTime(LocalDateTime localDateTime) {
        return localDateTime.format(DateTimeFormatter.ofPattern("yyyyddMMHHmmss"));
    }

    /**
     * Format date :  yyyy.dd.MM HH:mm:ss
     *
     * @param localDateTime - date and time
     * @return string type date
     */
    public static String getFormattedCheck(LocalDateTime localDateTime) {
        return localDateTime.format(DateTimeFormatter.ofPattern("yyyy.dd.MM HH:mm:ss"));
    }

    /**
     * Round sum
     *
     * @param sum - sum
     * @return round sum
     */
    public static BigDecimal round(BigDecimal sum) {
        return sum.setScale(2, BigDecimal.ROUND_HALF_EVEN);
    }

    /**
     * Format date : yyyyMMdd
     *
     * @param localDate - string date
     * @return type LocalDate
     */
    public static LocalDate getFormattedStringToLocalDate(String localDate) {
        return LocalDate.parse(localDate, DateTimeFormatter.ofPattern("yyyyMMdd"));
    }

    /**
     * Format date : yyyy-MM-dd HH:mm:ss
     *
     * @param localDateTime - date and time
     * @return string type date
     */
    public static LocalDateTime getFormattedStringToLocalDateTime(String localDateTime) {
        return LocalDateTime.parse(localDateTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    /**
     * Format date : yyyyMMdd
     *
     * @param date - date and time
     * @return string type date
     */
    public static String getFormattedLocalDateToString(LocalDate date) {
        return date.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
    }
}
