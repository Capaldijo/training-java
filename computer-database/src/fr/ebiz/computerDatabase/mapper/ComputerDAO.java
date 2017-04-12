package fr.ebiz.computerDatabase.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import fr.ebiz.computerDatabase.model.Computer;
import fr.ebiz.computerDatabase.persistence.DatabaseManager;
import fr.ebiz.computerDatabase.utils.Utils;

public class ComputerDAO {

	private DateTimeFormatter formatter;
	
	private String TABLE_NAME;
	
	public ComputerDAO() {
		formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S");
		TABLE_NAME = Utils.COMPUTER_TABLE;
	}
	
	public Computer find(int idComp) throws SQLException {
		String query = "SELECT * FROM "+ TABLE_NAME +" WHERE id = ?";
		
		ResultSet resultat = DatabaseManager.getInstance().getComputerById(query, idComp);
		resultat.next();
        
        Computer comp = this.fromDBToComputer(resultat);
		
		return comp;
	}
	
	public ResultSet findAll() throws SQLException {
		String query = "SELECT * FROM " + TABLE_NAME;
		return DatabaseManager.getInstance().execQuery(query);
	}
	
	public List<Computer> findByPage(int numPage, int nbLine) throws SQLException {
		String query = "SELECT * FROM " + TABLE_NAME + " LIMIT ?, ?";
		ResultSet resultat = DatabaseManager.getInstance().execQueryPageable(query, numPage, nbLine);
		List<Computer> list = new ArrayList<>();
		
		while(resultat.next()){
			list.add(fromDBToComputer(resultat));
		}
		
		return list;
	}
	
	public int insert(Computer comp) throws SQLException {
		String name = comp.getName();
        LocalDateTime dateIntro = comp.getIntroduced();
		LocalDateTime dateDiscon = comp.getDiscontinued();
        int compIdRef = comp.getCompany_id();
        
        String strDateIntro = null, strDateDiscon = null;
        
        if(dateIntro != null)
            strDateIntro = dateIntro.format(formatter);
        
        if(dateDiscon != null)
            strDateDiscon = dateDiscon.format(formatter);
        
        String query = "INSERT INTO " + TABLE_NAME + " (name,introduced,discontinued,company_id) VALUES (?, ?, ?, ?)";
        
        return DatabaseManager.getInstance().insertComputer(query, name, strDateIntro, strDateDiscon, compIdRef);
	}
	
	public int update(Computer comp) throws SQLException {
		int id = comp.getId();
		String name = comp.getName();
        LocalDateTime dateIntro = comp.getIntroduced();
		LocalDateTime dateDiscon = comp.getDiscontinued();
        int compIdRef = comp.getCompany_id();
        
        String strDateIntro = null, strDateDiscon = null;
        
        if(dateIntro != null)
            strDateIntro = dateIntro.format(formatter);
        
        if(dateDiscon != null) 
            strDateDiscon = dateDiscon.format(formatter);
            
        String query = "UPDATE " + TABLE_NAME + " SET name = ?, introduced = ?, discontinued = ?, company_id = ? WHERE id = ?";
        
        return DatabaseManager.getInstance().updateComputer(query, id, name, strDateIntro, strDateDiscon, compIdRef);
	}
	
	public int delete(int id) throws SQLException {
		String query = "DELETE FROM "+ TABLE_NAME + " WHERE id = ?";
		return DatabaseManager.getInstance().deleteComputer(query, id);
	}
	
	public Computer fromDBToComputer(ResultSet resultat) throws SQLException {
		int id = resultat.getInt(Utils.COLUMN_ID);
        String name = resultat.getString(Utils.COLUMN_NAME);
        String strDateIntro = resultat.getString(Utils.COLUMN_INTRODUCED);
        String strDateDiscon = resultat.getString(Utils.COLUMN_DISCONTINUED);
        int compIdRef = resultat.getInt(Utils.COLUMN_COMPANYID);
        
        LocalDateTime dateIntro = null, dateDiscon = null;
        
        if(strDateIntro != null)
        	dateIntro = LocalDateTime.parse(strDateIntro, formatter);
        
        if(strDateDiscon != null)
        	dateDiscon = LocalDateTime.parse(strDateDiscon, formatter);
        
		return new Computer(id, name, dateIntro, dateDiscon, compIdRef);
	}
}
