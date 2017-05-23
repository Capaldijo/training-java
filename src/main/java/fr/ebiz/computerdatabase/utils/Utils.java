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

    // ---- Var for FORMATTER in MAPPER ----

    String FORMATTER_DB = "yyyy-MM-dd HH:mm:ss.S";

    String FORMATTER_WEB = "yyyy-MM-dd";
}