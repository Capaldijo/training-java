package fr.ebiz.computerdatabase.mappers;

import fr.ebiz.computerdatabase.models.Computer;
import fr.ebiz.computerdatabase.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class ComputerRowMapper implements RowMapper<Computer> {

    private DateTimeFormatter formatterDB = DateTimeFormatter.ofPattern(Utils.FORMATTER_DB);;

    private static final Logger LOG = LoggerFactory.getLogger(ComputerRowMapper.class);

    @Override
    public Computer mapRow(ResultSet rs, int rowNum) throws SQLException {
        Computer computer = null;
        try {
            Long id = rs.getLong(Utils.COLUMN_ID);
            String name = rs.getString(Utils.COLUMN_NAME);
            String strDateIntro = rs.getString(Utils.COLUMN_INTRODUCED);
            String strDateDiscon = rs.getString(Utils.COLUMN_DISCONTINUED);
            int compIdRef = rs.getInt(Utils.COLUMN_COMPANYID);

            LocalDate dateIntro = null, dateDiscon = null;

            if (strDateIntro != null) {
                dateIntro = LocalDate.parse(strDateIntro, formatterDB);
            }

            if (strDateDiscon != null) {
                dateDiscon = LocalDate.parse(strDateDiscon, formatterDB);
            }

            computer = new Computer.Builder(name).introduced(dateIntro).discontinued(dateDiscon).companyId(compIdRef)
                    .id(id).build();

        } catch (SQLException | DateTimeParseException e) {
            LOG.error("[TOCOMPUTER] Error on parsing date.");
            throw new SQLException("[TOCOMPUTER] Error on parsing date.");
        }
        return computer;
    }
}
