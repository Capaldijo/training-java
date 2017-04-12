package fr.ebiz.computerDatabase.utils;

public interface Utils {

	public final String urlDB = "jdbc:mysql://localhost:3306/computer-database-db";
	
	public final String userDB = "admincdb";
	
	public final String passwdDB = "qwerty1234";
	
	public final String COMPANY_TABLE = "company";
	
	public final String COMPUTER_TABLE = "computer";
	
	public final String COLUMN_ID = "id";
	
	public final String COLUMN_NAME = "name";
	
	public final String COLUMN_INTRODUCED = "introduced";
	
	public final String COLUMN_DISCONTINUED = "discontinued";
	
	public final String COLUMN_COMPANYID = "company_id";
	
	public final int PAGEABLE_NBLINE = 10;
	
}
