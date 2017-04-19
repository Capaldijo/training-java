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

	public static String ADD_VIEW = "/WEB-INF/add_computer.jsp";
	
	public static String EDIT_VIEW = "/WEB-INF/edit_computer.jsp";
	
	// ---- Var for JSP ----
	
	public static String PARAM_COMPUTER_ID = "id";
	
	public static String PARAM_COMPUTER_NAME = "computerName";
	
	public static String PARAM_COMPUTER_INTRODUCED = "computerIntro";
	
	public static String PARAM_COMPUTER_DISCONTINUED = "computerDiscon";
	
	public static String PARAM_COMPUTER_COMPANYID = "companyId";
	
}
