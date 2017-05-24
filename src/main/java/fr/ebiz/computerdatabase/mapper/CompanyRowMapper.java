package fr.ebiz.computerdatabase.mapper;

import fr.ebiz.computerdatabase.model.Company;

import fr.ebiz.computerdatabase.util.Utils;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CompanyRowMapper implements RowMapper<Company> {

    @Override
    public Company mapRow(ResultSet rs, int rowNum) throws SQLException {
        Long id = rs.getLong(Utils.COLUMN_ID);
        String name = rs.getString(Utils.COLUMN_NAME);
        return new Company(id, name);
    }
}
