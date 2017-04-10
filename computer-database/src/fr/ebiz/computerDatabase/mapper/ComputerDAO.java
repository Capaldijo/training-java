package fr.ebiz.computerDatabase.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import fr.ebiz.computerDatabase.model.Computer;
import fr.ebiz.computerDatabase.persistence.DatabaseManager;
import fr.ebiz.computerDatabase.utils.Utils;

public class ComputerDAO {

	public ComputerDAO(){}
	
	public  Computer getComputer(int idComp) throws SQLException {
		
		ResultSet resultat = DatabaseManager.getInstance().getComputerById(idComp);
		resultat.next();
		
		int id = resultat.getInt(Utils.COLUMN_ID);
        String name = resultat.getString(Utils.COLUMN_NAME);
        String strDateIntro = resultat.getString(Utils.COLUMN_INTRODUCED);
        String strDateDiscon = resultat.getString(Utils.COLUMN_DISCONTINUED);
        int compIdRef = resultat.getInt(Utils.COLUMN_COMPANYID);
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime dateIntro = null, dateDiscon = null;
        
        if(strDateIntro != null)
        	dateIntro = LocalDateTime.parse(strDateIntro, formatter);
        
        if(strDateDiscon != null)
        	dateDiscon = LocalDateTime.parse(strDateDiscon, formatter);
        
        Computer comp = new Computer(id, name, dateIntro, dateDiscon, compIdRef);
		
		return comp;
	}
	
	public ResultSet getAllComputer() throws SQLException{
		return DatabaseManager.getInstance().getComputers();
	}
	
	public void createComputer(Computer comp) {
		
	}
	
	public void updateComputer(Computer comp) {
		
	}
	
	public void deleteComputer(Computer comp) {
		
	}
}
