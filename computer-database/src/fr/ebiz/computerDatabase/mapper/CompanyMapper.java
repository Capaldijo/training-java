package fr.ebiz.computerDatabase.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.ebiz.computerDatabase.model.Company;
import fr.ebiz.computerDatabase.utils.Utils;

public class CompanyMapper {
	
	final Logger logger = LoggerFactory.getLogger(CompanyMapper.class);
	
	public CompanyMapper() {}
	
	/*
	 * get a company from the database and build it
	 * as an instance of Company class
	 * 
	 * return a company object
	 */
	public Company fromDBToCompany(ResultSet resultat) throws SQLException {
		int id = resultat.getInt(Utils.COLUMN_ID);
        String name = resultat.getString(Utils.COLUMN_NAME);
        
		return new Company(id, name);
	}
	
	public List<Company> fromDBToCompanies(ResultSet resultat) {
		List<Company> list = new ArrayList<>();
		
		try{
			while(resultat.next()){
				list.add(fromDBToCompany(resultat));
			}
		} catch(SQLException sqle){
			logger.error("Error on reading data from db.");
		}
		
		return list;
	}
}
