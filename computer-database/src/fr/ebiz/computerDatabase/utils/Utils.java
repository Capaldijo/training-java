package fr.ebiz.computerDatabase.utils;

public interface Utils {

    // ---- Var For DataBase

    String URLDB = "jdbc:mysql://localhost:3306/computer-database-db";

    String USERDB = "admincdb";

    String PASSWDDB = "qwerty1234";

    String COMPANY_TABLE = "company";

    String COMPUTER_TABLE = "computer";

    String COLUMN_ID = "id";

    String COLUMN_NAME = "name";

    String COLUMN_INTRODUCED = "introduced";

    String COLUMN_DISCONTINUED = "discontinued";

    String COLUMN_COMPANYID = "company_id";

    int PAGEABLE_NBLINE = 10;

    // ---- Var for Servlet ----

    String DASHBOARD_VIEW = "/WEB-INF/dashboard.jsp";

    String ADD_VIEW = "/WEB-INF/add_computer.jsp";

    String EDIT_VIEW = "/WEB-INF/edit_computer.jsp";

    // ---- Var for JSP ----

    String PARAM_COMPUTER_ID = "id";

    String PARAM_COMPUTER_NAME = "computerName";

    String PARAM_COMPUTER_INTRODUCED = "computerIntro";

    String PARAM_COMPUTER_DISCONTINUED = "computerDiscon";

    String PARAM_COMPUTER_COMPANYID = "companyId";

}