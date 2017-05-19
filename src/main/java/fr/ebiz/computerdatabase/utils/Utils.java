package fr.ebiz.computerdatabase.utils;

import fr.ebiz.computerdatabase.exceptions.DAOException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public interface Utils {

    // ---- Var For DataBase

    String COMPANY_TABLE = "company";

    String COMPUTER_TABLE = "computer";

    String COLUMN_ID = "id";

    String COLUMN_NAME = "name";

    String COLUMN_INTRODUCED = "introduced";

    String COLUMN_DISCONTINUED = "discontinued";

    String COLUMN_COMPANYID = "company_id";

    int PAGEABLE_NBLINE = 10;

    // ---- Var for JSP ----

    String PARAM_COMPUTER_ID = "id";

    String PARAM_COMPUTER_NAME = "computerName";

    String PARAM_COMPUTER_INTRODUCED = "computerIntro";

    String PARAM_COMPUTER_DISCONTINUED = "computerDiscon";

    String PARAM_COMPUTER_COMPANYID = "companyId";

    String[] HEADERTABLE_NAME = {null, "c.name", "c.introduced", "c.discontinued", "c.company_id"};

    // ---- Var for FORMATTER in MAPPER ----

    String FORMATTER_DB = "yyyy-MM-dd HH:mm:ss.S";

    String FORMATTER_WEB = "yyyy-MM-dd";

    /**
     *
     * @param st statement to close.
     * @param rs resultSet to close.
     * @throws DAOException exception to throw
     */
    static void closeObjects(Statement st, ResultSet rs) throws DAOException {

        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                throw new DAOException(e.getMessage());
            }
        }
        if (st != null) {
            try {
                st.close();
            } catch (SQLException e) {
                throw new DAOException(e.getMessage());
            }
        }
    }
}