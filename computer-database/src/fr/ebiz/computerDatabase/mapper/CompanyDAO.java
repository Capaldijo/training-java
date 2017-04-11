package fr.ebiz.computerDatabase.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import fr.ebiz.computerDatabase.model.Company;
import fr.ebiz.computerDatabase.persistence.DatabaseManager;
import fr.ebiz.computerDatabase.utils.Utils;

public class CompanyDAO {
	
	private String TABLE_NAME;
	
	public CompanyDAO() {
		TABLE_NAME = Utils.COMPANY_TABLE;
	}
	
	public ResultSet getAllCompanies() throws SQLException{
		String query = "SELECT * FROM " + TABLE_NAME;
		return DatabaseManager.getInstance().getCompanies(query);
	}
	
	public Company getCompany(int id) throws SQLException {
		String query = "SELECT * FROM "+ TABLE_NAME +" WHERE id = ?";
		
		ResultSet resultat = DatabaseManager.getInstance().getCompanyById(query, id);
		resultat.next();
		
		int idComp = resultat.getInt(Utils.COLUMN_ID);
        String name = resultat.getString(Utils.COLUMN_NAME);
        
        Company comp = new Company(idComp, name);
		
		return comp;
	}

}
