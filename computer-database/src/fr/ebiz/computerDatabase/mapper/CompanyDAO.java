package fr.ebiz.computerDatabase.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import fr.ebiz.computerDatabase.model.Company;
import fr.ebiz.computerDatabase.model.Computer;
import fr.ebiz.computerDatabase.persistence.DatabaseManager;
import fr.ebiz.computerDatabase.utils.Utils;

public class CompanyDAO {
	
	private String TABLE_NAME;
	
	public CompanyDAO() {
		TABLE_NAME = Utils.COMPANY_TABLE;
	}
	
	public ResultSet findAll() throws SQLException{
		String query = "SELECT * FROM " + TABLE_NAME;
		return DatabaseManager.getInstance().execQuery(query);
	}
	
	public Company find(int id) throws SQLException {
		String query = "SELECT * FROM "+ TABLE_NAME +" WHERE id = ?";
		
		ResultSet resultat = DatabaseManager.getInstance().getCompanyById(query, id);
		resultat.next();
		
		int idComp = resultat.getInt(Utils.COLUMN_ID);
        String name = resultat.getString(Utils.COLUMN_NAME);
        
        Company comp = new Company(idComp, name);
		
		return comp;
	}

	public List<Company> findByPage(int numPage, int nbLine) throws SQLException {
		String query = "SELECT * FROM " + TABLE_NAME + " LIMIT ?, ?";
		ResultSet resultat = DatabaseManager.getInstance().execQueryPageable(query, numPage, nbLine);
		List<Company> list = new ArrayList<>();
		
		while(resultat.next()){
			list.add(fromDBToComputer(resultat));
		}
		
		return list;
	}
	
	public Company fromDBToComputer(ResultSet resultat) throws SQLException {
		int id = resultat.getInt(Utils.COLUMN_ID);
        String name = resultat.getString(Utils.COLUMN_NAME);
        
		return new Company(id, name);
	}
}
