package com.epam.esm.rowmapper;

import com.epam.esm.impl.ColumnName;
import com.epam.esm.model.GiftCertificate;
import org.springframework.jdbc.core.RowMapper;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class GiftCertificateRowMapper implements RowMapper<GiftCertificate> {

    @Override
    public GiftCertificate mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new GiftCertificate(
                rs.getLong(ColumnName.GIFT_CERTIFICATE_ID),
                rs.getString(ColumnName.GIFT_CERTIFICATE_NAME),
                rs.getString(ColumnName.GIFT_CERTIFICATE_DESCRIPTION),
                rs.getBigDecimal(ColumnName.GIFT_CERTIFICATE_PRICE),
                rs.getInt(ColumnName.GIFT_CERTIFICATE_DURATION),
                rs.getObject(ColumnName.GIFT_CERTIFICATE_CREATION_DATE, LocalDateTime.class),
                rs.getObject(ColumnName.GIFT_CERTIFICATE_LAST_UPDATE_DATE, LocalDateTime.class)
        );
    }
}