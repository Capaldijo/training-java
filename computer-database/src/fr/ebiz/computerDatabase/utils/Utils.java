package fr.ebiz.computerDatabase.utils;

public interface Utils {

	// ---- Var For DataBase
	public static final String urlDB = "jdbc:mysql://localhost:3306/computer-database-db";

	public static final String userDB = "admincdb";

	public static final String passwdDB = "qwerty1234";

	public static final String COMPANY_TABLE = "company";

	public static final String COMPUTER_TABLE = "computer";

	public static final String COLUMN_ID = "id";

	public static final String COLUMN_NAME = "name";

	public static final String COLUMN_INTRODUCED = "introduced";

	public static final String COLUMN_DISCONTINUED = "discontinued";

	public static final String COLUMN_COMPANYID = "company_id";

	public static final int PAGEABLE_NBLINE = 12;

	// ---- Var for Servlet ----

	public static String DASHBOARD_VIEW = "/WEB-INF/dashboard.jsp";

	public static String ADD_VIEW = "/WEB-INF/addComputer.jsp";
}
