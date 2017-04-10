package fr.ebiz.computerDatabase.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import fr.ebiz.computerDatabase.model.Computer;
import fr.ebiz.computerDatabase.persistence.DatabaseManager;
import fr.ebiz.computerDatabase.utils.Utils;

public class ComputerDAO {

	public ComputerDAO(){}
	
	public  Computer getComputer(int id) throws SQLException {
		Computer comp = new Computer();
		
		ResultSet resultat = DatabaseManager.getInstance().getComputerById(id);
		resultat.next();
		int idComp = resultat.getInt(Utils.COLUMN_ID);
        String nameComp = resultat.getString(Utils.COLUMN_NAME);
        int compIdRef = resultat.getInt(Utils.COLUMN_COMPANYID);
        
        comp.setId(idComp);
        comp.setName(nameComp);
        comp.setCompany_id(compIdRef);
        //comp.setIntroduced();
        //comp.setDiscontinued();
		
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
