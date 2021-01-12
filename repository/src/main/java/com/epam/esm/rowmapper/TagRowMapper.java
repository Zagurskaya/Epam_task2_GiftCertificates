package com.epam.esm.rowmapper;

import com.epam.esm.impl.ColumnName;
import com.epam.esm.model.Tag;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TagRowMapper implements RowMapper<Tag> {

    @Override
    public Tag mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Tag(
                rs.getLong(ColumnName.TAG_ID),
                rs.getString(ColumnName.TAG_NAME)
        );
    }
}